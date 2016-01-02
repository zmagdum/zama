package org.zama.examples.multitenant.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * DatabaseConfiguration.
 *
 * @author Zakir Magdum
 */
@Configuration
@EnableJpaRepositories("org.zama.examples.multitenant.repository")
@EnableTransactionManagement
public class DatabaseConfiguration {
    private final static Logger LOGGER = LoggerFactory.getLogger(DatabaseConfiguration.class);

    @Value("${liquibase.context}")
    private String liquibaseContext;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.dataSourceClassName}")
    private String dataSourceClassName;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean(destroyMethod = "close")
    public DataSource dataSource() {
        LOGGER.debug("Configuring datasource {} {} {}", dataSourceClassName, url, user);
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(dataSourceClassName);
        config.addDataSourceProperty("url", url);
        config.addDataSourceProperty("user", user);
        config.addDataSourceProperty("password", password);
        return new HikariDataSource(config);
    }

    @Bean
    public SpringLiquibase liquibase(DataSource dataSource) {
        SpringLiquibase sl = new SpringLiquibase();
        sl.setDataSource(dataSource);
        sl.setContexts(liquibaseContext);
        sl.setChangeLog("classpath:dbchangelog.xml");
        sl.setShouldRun(true);
        return sl;
    }
}
