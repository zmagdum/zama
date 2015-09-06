package org.zama.examples.liquibase.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zama.examples.liquibase.model.Company;
import org.zama.examples.liquibase.repository.CompanyRepository;
import org.zama.examples.liquibase.repository.RoleRepository;
import org.zama.examples.liquibase.repository.UserRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * UserService.
 *
 * @author Zakir Magdum
 */
@Service
@Transactional
public class UserService {
    @Inject
    private UserRepository userRepository;
    @Inject
    private CompanyRepository companyRepository;
    @Inject
    private RoleRepository roleRepository;

    public Company registerCompany(Company company) {
        Optional<Company> exists = companyRepository.findOneByName(company.getName());
        return exists.isPresent() ? companyRepository.save(exists.get().merge(company))
                : companyRepository.save(company);
    }

    public void removeCompany(String companyName) {
        Optional<Company> exists = companyRepository.findOneByName(companyName);
        if (exists.isPresent()) {
            removeCompany(exists.get());
        }
    }

    public void removeCompany(Company company) {
        companyRepository.delete(company);
    }

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

}
