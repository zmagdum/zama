package org.zama.examples.multitenant.repository.master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zama.examples.multitenant.model.master.Company;

import java.util.Optional;

/**
 * CompanyRepository.
 * @author Zakir Magdum
 */
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findOneByName(String name);
    Optional<Company> findOneByCompanyKey(String companyKey);
}
