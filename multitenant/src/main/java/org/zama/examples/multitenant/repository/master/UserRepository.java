package org.zama.examples.multitenant.repository.master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zama.examples.multitenant.model.master.User;

import java.util.Optional;

/**
 * UserRepository.
 * @author Zakir Magdum
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByName(String name);
}
