package org.zama.examples.liquibase.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zama.examples.liquibase.model.Company;
import org.zama.examples.liquibase.model.Role;

import java.util.Optional;

/**
 * RoleRepository.
 * @author Zakir Magdum
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findOneByName(String name);
    Optional<Company> findOneByCompanyKey(String companyKey);
}
