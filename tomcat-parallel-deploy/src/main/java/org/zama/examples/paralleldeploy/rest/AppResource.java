package org.zama.examples.paralleldeploy.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * AppResource.
 *
 * @author Zakir Magdum
 */
@RestController
@RequestMapping("/")
public class AppResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppResource.class);

    @RequestMapping("/resource")
    public Map<String,Object> home() {
        Map<String,Object> model = new HashMap<String,Object>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "Hello World");
        return model;
    }

    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

//    @RequestMapping("/login")
//    public String login() {
//        return "forward:/";
//    }
}
