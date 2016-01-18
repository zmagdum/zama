package org.zama.examples.multitenant.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.zama.examples.multitenant.model.Company;
import org.zama.examples.multitenant.model.Product;
import org.zama.examples.multitenant.model.ProductDTO;
import org.zama.examples.multitenant.model.User;
import org.zama.examples.multitenant.repository.CompanyRepository;
import org.zama.examples.multitenant.repository.ProductRepository;
import org.zama.examples.multitenant.repository.UserRepository;

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
public class HelloWorldResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldResource.class);
    @Resource
    private ProductRepository productRepository;

    @Resource
    private UserRepository userRepository;

    @Resource
    private CompanyRepository companyRepository;

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
        List<Product> products = productRepository.findByCompanyKey(user.getCompany().getCompanyKey());
        LOGGER.info("Found products {} {}", user.getCompany().getName(), products.size());
        return products;
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public @ResponseBody Product saveProduct(@RequestBody ProductDTO product) {
        Optional<Company> comp = companyRepository.findOneByCompanyKey(product.getCompanyId());
        Optional<Product> pp = productRepository.findOneByName(product.getName());
        Product prod = pp.isPresent() ? pp.get() : new Product();
        prod.setName(product.getName());
        prod.setDescription(product.getDescription());
        prod.setPrice(product.getPrice());
        prod.setProductId(product.getProductId());
        if (comp.isPresent()) {
            prod.setCompany(comp.get());
        }
        return productRepository.save(prod);
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
