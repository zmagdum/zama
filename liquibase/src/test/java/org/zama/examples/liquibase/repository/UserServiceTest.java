package org.zama.examples.liquibase.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.zama.examples.liquibase.Application;
import org.zama.examples.liquibase.service.UserService;
import org.zama.examples.liquibase.model.Company;

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
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest
@Transactional
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
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.isEnabled()).isFalse();

        company.setCompanyKey("fake1");
        company.setEnabled(true);
        company.setUpdated(LocalDateTime.now());
        saved = userService.registerCompany(company);
        assertThat(saved.isEnabled()).isTrue();
        assertThat(company.getCompanyKey().equals(saved.getCompanyKey())).isTrue();
        assertThat(company.getUpdated().equals(saved.getUpdated())).isTrue();

        userService.removeCompany(company.getName());
        List<Company> companies = userService.findAllCompanies();
        assertThat(companies.size() == 0).isTrue();
    }
}