package org.zama.examples.multitenant.config;

import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.Environment;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.zama.examples.multitenant.model.tenant.Product;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MultiTenancyJpaConfiguration.
 *
 * @author Zakir Magdum
 */
@Configuration
@ComponentScan("org.zama.examples.multitenant.add")
@EnableConfigurationProperties(JpaProperties.class)
@EnableJpaRepositories(
        entityManagerFactoryRef = "tenantEntityManager",
        transactionManagerRef = "tenantTransactionManager",
        basePackages = {"org.zama.examples.multitenant.repository.tenant"})
@EnableTransactionManagement
public class MultiTenancyJpaConfiguration {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JpaProperties jpaProperties;

    @Autowired
    private MultiTenantConnectionProvider multiTenantConnectionProvider;

    @Autowired
    private CurrentTenantIdentifierResolver currentTenantIdentifierResolver;

    @Bean(name = "tenantEntityManager")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String, Object> hibernateProps = new LinkedHashMap<>();
        hibernateProps.putAll(jpaProperties.getHibernateProperties(dataSource));

        hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.DATABASE);
        hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, currentTenantIdentifierResolver);
        hibernateProps.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");

        return builder.dataSource(dataSource).packages(Product.class.getPackage().getName())
                .properties(hibernateProps).jta(false).build();
    }

    @Bean(name = "tenantTransactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory tenantEntityManager){
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(tenantEntityManager);
        return transactionManager;
    }
}