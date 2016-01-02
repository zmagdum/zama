package org.zama.examples.multitenant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zama.examples.multitenant.model.Role;

import java.util.Optional;

/**
 * RoleRepository.
 * @author Zakir Magdum
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findOneByName(String name);
}
