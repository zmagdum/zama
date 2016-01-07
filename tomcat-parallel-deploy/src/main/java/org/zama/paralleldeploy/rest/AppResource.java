package org.zama.paralleldeploy.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * AppResource.
 * Controller for generic functionality for the app.
 *
 * @author Zakir Magdum
 */
@RestController
@RequestMapping("/")
public class AppResource {
    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}
