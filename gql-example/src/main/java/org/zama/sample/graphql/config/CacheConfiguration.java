package org.zama.sample.graphql.config;

import io.github.jhipster.config.JHipsterProperties;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.expiry.Duration;
import org.ehcache.expiry.Expirations;
import org.ehcache.jsr107.Eh107Configuration;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

@Configuration
@EnableCaching
@AutoConfigureAfter(value = { MetricsConfiguration.class })
@AutoConfigureBefore(value = { WebConfigurer.class, DatabaseConfiguration.class })
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache =
            jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                .withExpiry(Expirations.timeToLiveExpiration(Duration.of(ehcache.getTimeToLiveSeconds(), TimeUnit.SECONDS)))
                .build());
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            cm.createCache(org.zama.sample.graphql.domain.User.class.getName(), jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.Authority.class.getName(), jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.User.class.getName() + ".authorities", jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.PersistentToken.class.getName(), jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.User.class.getName() + ".persistentTokens", jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.Customer.class.getName(), jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.Customer.class.getName() + ".orders", jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.CustomerOrder.class.getName(), jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.CustomerOrder.class.getName() + ".items", jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.Department.class.getName(), jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.Department.class.getName() + ".departments", jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.Location.class.getName(), jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.OrderItem.class.getName(), jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.Product.class.getName(), jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.Product.class.getName() + ".items", jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.SalesPerson.class.getName(), jcacheConfiguration);
            cm.createCache(org.zama.sample.graphql.domain.SalesPerson.class.getName() + ".salesPeople", jcacheConfiguration);
            // jhipster-needle-ehcache-add-entry
        };
    }
}
