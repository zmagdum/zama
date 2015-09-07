package org.zama.examples.liquibase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zama.examples.liquibase.model.Company;
import org.zama.examples.liquibase.model.Role;
import org.zama.examples.liquibase.model.User;

import java.util.Optional;

/**
 * RoleRepository.
 * @author Zakir Magdum
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByName(String name);
}
