package org.zama.examples.paralleldeploy.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zama.examples.paralleldeploy.model.UploadMessage;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * FileUploadController.
 *
 * @author Zakir Magdum
 */
@Controller
public class FileUploadController implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);
    public static final String WAR_WITH_VERSION_FORMAT = "%s##%03d.%s";
    private Path downloadDirectory;
    private Path webappsDirectory;

    @Value("${app.removeOldVersions}")
    private boolean removeOldVersions;

    @RequestMapping(value="/api/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a war file by posting to this same URL.";
    }

    @RequestMapping(value="/api/upload", method= RequestMethod.POST)
    public @ResponseBody UploadMessage handleFileUpload(@RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                Path path = Paths.get(downloadDirectory.toString(), file.getOriginalFilename());
                Files.write(path, file.getBytes());
                LOGGER.info("File upload {} {}", file.getOriginalFilename(), path);
                String ext = "";
                String name = file.getOriginalFilename();
                int ii = file.getOriginalFilename().lastIndexOf('.');
                if (ii >= 0) {
                    ext = file.getOriginalFilename().substring(ii + 1);
                    name = file.getOriginalFilename().substring(0, ii);
                }
                List<Path> files = new ArrayList<>();
                int version = nextVersionNumber(name, ext, files);
                Path deploy = Paths.get(webappsDirectory.toString(),
                        String.format(WAR_WITH_VERSION_FORMAT, name, version, ext));
                LOGGER.info("Deploying file {}", deploy);
                Files.copy(path, deploy);
                LOGGER.info("Deployed file {} {}", deploy, removeOldVersions);
                if (removeOldVersions) {
                    for (Path old : files) {
                        Files.delete(old);
                        LOGGER.info("Removed old deployment {}", old);
                    }
                }
                return new UploadMessage("You successfully uploaded " + file.getOriginalFilename() + "!");
            } catch (Exception e) {
                LOGGER.error("File upload error {} {}", file.getOriginalFilename(), e);
                return new UploadMessage("You failed to upload " + file.getOriginalFilename() + " => " + e.getMessage());
            }
        } else {
            LOGGER.error("You failed to upload " + file.getOriginalFilename() + " because the file was empty.");
            return new UploadMessage("You failed to upload " + file.getOriginalFilename() + " because the file was empty.");
        }
    }

    @RequestMapping(value="/api/remove/old/{appName}", method= RequestMethod.GET)
    public @ResponseBody UploadMessage removeOldVersion(@PathVariable String appName){
        try {
            LOGGER.info("Removing old applications for {}", appName);
            String ext = "war";
            String name = appName;
            int ii = appName.lastIndexOf('.');
            if (ii >= 0) {
                ext = appName.substring(ii + 1);
                name = appName.substring(0, ii);
            }
            List<Path> files = new ArrayList<>();
            int version = nextVersionNumber(name, ext, files);
            version--;
            String toSave = String.format(WAR_WITH_VERSION_FORMAT, name, version, ext);
            for (Path old : files) {
                if (!old.endsWith(toSave)) {
                    Files.delete(old);
                    LOGGER.info("Removed old deployment {}", old);
                }
            }
            return new UploadMessage("Removed old " + String.valueOf(files.size()-1) + " versions");
        } catch (Exception e) {
            LOGGER.error("Error removing old versions {}", appName, e);
            return new UploadMessage("Error removing old versions " + appName + " => " + e.getMessage());
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        downloadDirectory = Paths.get(System.getProperty("catalina.home"), "downloads");
        Files.createDirectories(downloadDirectory);
        webappsDirectory = Paths.get(System.getProperty("catalina.home"), "webapps");
    }

    private int nextVersionNumber(String name, String ext, List<Path> files) {
        int version = 0;
         try (DirectoryStream<Path> stream = Files.newDirectoryStream(webappsDirectory,
                String.format("%s*.%s",name, ext))) {
            for (Path entry: stream) {
                LOGGER.info("Found existing deployment {}", entry);
                int ii = entry.getFileName().toString().lastIndexOf('.');
                if (ii > 0) {
                    String ver = entry.getFileName().toString().substring(0, ii);
                    ii = ver.indexOf("##");
                    if (ii > 0) {
                        ver = ver.substring(ii+2);
                    }
                    version = Math.max(version, Integer.parseInt(ver));
                }
                files.add(entry);
            }
        } catch (Exception ex) {
            LOGGER.error("Error listing webapps directory {}", webappsDirectory, ex);
        }
        version++;
        return version;
    }

}