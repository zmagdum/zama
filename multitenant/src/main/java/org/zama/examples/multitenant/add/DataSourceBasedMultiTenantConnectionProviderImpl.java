package org.zama.examples.multitenant.add;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.zama.examples.multitenant.model.master.Company;
import org.zama.examples.multitenant.repository.master.CompanyRepository;
import org.zama.examples.multitenant.util.Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * DataSourceBasedMultiTenantConnectionProviderImpl.
 *
 * @author Zakir Magdum
 */
@Component
@Transactional(value="masterTransactionManager", readOnly = true)
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl implements ApplicationListener<ContextRefreshedEvent> {
    private final static Logger LOGGER = LoggerFactory.getLogger(DataSourceBasedMultiTenantConnectionProviderImpl.class);
    private static final String DEFAULT_TENANT_ID = "tenant_1";

    private Map<String, DataSource> map;

    @Inject
    private CompanyRepository companyRepository;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.dataSourceClassName}")
    private String dataSourceClassName;

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Inject
    private DataSource dataSource;

    @PostConstruct
    public void load() {
        map = new HashMap<>();
    }

    public void init() {
        for (Company company : companyRepository.findAll()) {
            // in this experiment we are just using once instance of mysql and simple of replacing master database
            // name with company key to get new database name
            try {
                String companyDbUrl = url.replace(Utils.databaseNameFromJdbcUrl(url), company.getCompanyKey());
                LOGGER.debug("Configuring datasource {} {} {}", dataSourceClassName, companyDbUrl, user);
                HikariConfig config = new HikariConfig();
                config.setDataSourceClassName(dataSourceClassName);
                config.addDataSourceProperty("url", companyDbUrl);
                config.addDataSourceProperty("user", user);
                config.addDataSourceProperty("password", password);
                HikariDataSource ds = new HikariDataSource(config);
                map.put(company.getCompanyKey(), ds);
                Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(
                        new JdbcConnection(ds.getConnection()));

                Liquibase liquibase = new Liquibase("dbchangelog-product.xml"
                        , new ClassLoaderResourceAccessor()
                        , database
                );
                liquibase.update("test, production");
            } catch (Exception e) {
                LOGGER.error("Error in database URL {}", url, e);
            }
        }
    }

    @Override
    protected DataSource selectAnyDataSource() {
        LOGGER.info("######### Selecting any data source");
        return dataSource; //map.get(DEFAULT_TENANT_ID);
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        LOGGER.info("+++++++++++ Selecting data source for {}", tenantIdentifier);
        return map.containsKey(tenantIdentifier) ? map.get(tenantIdentifier) : dataSource ;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        init();
    }
}
