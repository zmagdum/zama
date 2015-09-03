package org.zama.examples.liquibase.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.zama.examples.liquibase.Application;
import org.zama.examples.liquibase.model.Role;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RoleRepositoryTest.
 *
 * @author Zakir Magdum
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest
@Transactional
public class RoleRepositoryTest {

    @Inject
    private RoleRepository roleRepository;

    @Test
    public void testRemoveOldPersistentTokens() {
        Role role = new Role();
        role.setName("ROLE_TEST");
        role.setDescription("Test Role");

        Role saved = roleRepository.save(role);

        assertThat(saved.getId()).isNotNull();

        Role read = roleRepository.getOne(saved.getId());

        assertThat(role.getName().equals(read.getName())).isTrue();
        assertThat(role.getDescription().equals(read.getDescription())).isTrue();

    }
}