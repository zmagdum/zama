package org.zama.examples.multitenant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.zama.examples.multitenant.add.MultiTenancyInterceptor;

import javax.inject.Inject;

/**
 * WebMvcConfig.
 *
 * @author Zakir Magdum
 */
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Inject
    private MultiTenancyInterceptor multiTenancyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(multiTenancyInterceptor);
    }
}