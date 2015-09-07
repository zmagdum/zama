package org.zama.examples.liquibase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.zama.examples.liquibase.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Application.
 *
 * @author Zakir Magdum
 */
@ComponentScan
@EnableAutoConfiguration
public class Application {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.setShowBanner(false);
    }
}



