package org.zama.examples.multitenant.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zama.examples.multitenant.model.master.Company;
import org.zama.examples.multitenant.model.tenant.Product;
import org.zama.examples.multitenant.model.master.User;
import org.zama.examples.multitenant.repository.tenant.ProductRepository;
import org.zama.examples.multitenant.repository.master.UserRepository;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.*;

/**
 * HelloWorldResource.
 *
 * @author Zakir Magdum
 */
@RestController
@RequestMapping("/")
public class MultitenantResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(MultitenantResource.class);
    @Resource
    private ProductRepository productRepository;

    @Resource
    private UserRepository userRepository;

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
        List<Product> products = productRepository.findAll();
        LOGGER.info("Found products {} {}", user.getCompany().getName(), products.size());
        return products;
    }

    @RequestMapping("/companyByUserName/{userName}")
    public Company companyForUser(@PathVariable String userName) {
        Optional<User> user = userRepository.findOneByName(userName);
        if (user.isPresent()) {
            return user.get().getCompany();
        }
        return null;
    }

//    @RequestMapping("/login")
//    public String login() {
//        return "forward:/";
//    }
}
