package org.zama.examples.liquibase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zama.examples.liquibase.model.Role;

/**
 * RoleRepository.
 * @author Zakir Magdum
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
}
