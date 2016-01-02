package org.zama.examples.multitenant.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zama.examples.multitenant.model.Product;
import org.zama.examples.multitenant.model.User;
import org.zama.examples.multitenant.repository.ProductRepository;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * HelloWorldResource.
 *
 * @author Zakir Magdum
 */
@RestController
@RequestMapping("/")
public class HelloWorldResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldResource.class);
    @Resource
    private ProductRepository productRepository;

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

    @RequestMapping("/products")
    public List<Product> froductsForLoggerInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        LOGGER.info("Getting products for user {} company {}", user.getName(), user.getCompany().getCompanyKey());
        return productRepository.findByCompanyKey(user.getCompany().getCompanyKey());
    }

//    @RequestMapping("/login")
//    public String login() {
//        return "forward:/";
//    }
}
