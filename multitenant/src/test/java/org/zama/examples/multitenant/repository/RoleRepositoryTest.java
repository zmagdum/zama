package org.zama.examples.multitenant.repository;

import org.assertj.core.api.StrictAssertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.zama.examples.multitenant.MultitenantApplication;
import org.zama.examples.multitenant.model.Role;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * RoleRepositoryTest.
 *
 * @author Zakir Magdum
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MultitenantApplication.class)
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

        StrictAssertions.assertThat(saved.getId()).isNotNull();

        Role read = roleRepository.getOne(saved.getId());

        StrictAssertions.assertThat(role.getName().equals(read.getName())).isTrue();
        StrictAssertions.assertThat(role.getDescription().equals(read.getDescription())).isTrue();

    }
}