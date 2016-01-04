package org.zama.examples.paralleldeploy.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zama.examples.paralleldeploy.model.UploadMessage;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * FileUploadController.
 *
 * @author Zakir Magdum
 */
@Controller("/api")
public class FileUploadController implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUploadController.class);
    private Path downloadDirectory;

    @RequestMapping(value="/api/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value="/api/upload", method= RequestMethod.POST)
    public @ResponseBody UploadMessage handleFileUpload(@RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                Path path = Paths.get(downloadDirectory.toString(), file.getOriginalFilename());
                Files.write(path, file.getBytes());
                LOGGER.info("File upload {} {}", file.getOriginalFilename(), path);
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

    @Override
    public void afterPropertiesSet() throws Exception {
        downloadDirectory = Paths.get(System.getProperty("catalina.home"), "downloads");
        Files.createDirectories(downloadDirectory);
    }
}