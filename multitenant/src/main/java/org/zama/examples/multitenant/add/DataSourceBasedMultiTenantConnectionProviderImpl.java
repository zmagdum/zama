package org.zama.examples.multitenant.add;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.zama.examples.multitenant.model.master.Company;
import org.zama.examples.multitenant.repository.master.CompanyRepository;

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
public class DataSourceBasedMultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
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
        for (Company company : companyRepository.findAll()) {
            // in this experiment we are just using once instance of mysql and simple of replacing master database
            // name with company key to get new database name
            try {
                URI uri = new URI(url);
                String companyDbUrl = url.replace(uri.getPath(), company.getCompanyKey());
                LOGGER.debug("Configuring datasource {} {} {}", dataSourceClassName, companyDbUrl, user);
                HikariConfig config = new HikariConfig();
                config.setDataSourceClassName(dataSourceClassName);
                config.addDataSourceProperty("url", url);
                config.addDataSourceProperty("user", user);
                config.addDataSourceProperty("password", password);
                map.put(company.getCompanyKey(), new HikariDataSource(config));
            } catch (URISyntaxException e) {
                LOGGER.error("Error in database URL {}", url, e);
            }

        }
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return dataSource; //map.get(DEFAULT_TENANT_ID);
    }

    @Override
    protected DataSource selectDataSource(String tenantIdentifier) {
        return dataSource ; //map.get(tenantIdentifier);
    }
}
