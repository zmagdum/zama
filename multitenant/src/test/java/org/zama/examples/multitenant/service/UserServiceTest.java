package org.zama.examples.multitenant.service;

import org.assertj.core.api.StrictAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.repository.config.RepositoryConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.zama.examples.multitenant.config.SecurityConfiguration;
import org.zama.examples.multitenant.model.master.Company;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * UserServiceTest.
 *
 * @author Zakir Magdum
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan(basePackages = "org.zama.examples.multitenant.service")
@SpringApplicationConfiguration(classes = {RepositoryConfiguration.class, SecurityConfiguration.class})
@Transactional("masterTransactionManager")
public class UserServiceTest {

    @Inject
    private UserService userService;

    @Test
    public void testAddUpdateRemoveCompany() {
        Company company = new Company();
        company.setName("Fake Enterprise");
        company.setDescription("Some Description");
        company.setAddress("200 Bold Way, Nanjung TP 23405");
        company.setEnabled(false);
        company.setCompanyKey("fake");
        LocalDateTime dateTime = LocalDateTime.now();
        company.setCreated(dateTime);
        company.setUpdated(dateTime);
        Company saved = userService.registerCompany(company);
        StrictAssertions.assertThat(saved.getId()).isNotNull();
        StrictAssertions.assertThat(saved.isEnabled()).isFalse();

        company.setCompanyKey("fake1");
        company.setEnabled(true);
        company.setUpdated(LocalDateTime.now());
        saved = userService.registerCompany(company);
        StrictAssertions.assertThat(saved.isEnabled()).isTrue();
        StrictAssertions.assertThat(company.getCompanyKey().equals(saved.getCompanyKey())).isTrue();
        StrictAssertions.assertThat(company.getUpdated().equals(saved.getUpdated())).isTrue();

        userService.removeCompany(company.getName());
        List<Company> companies = userService.findAllCompanies();
        StrictAssertions.assertThat(companies.size() == 0).isTrue();
    }
}