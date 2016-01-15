package org.zama.examples.multitenant.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zama.examples.multitenant.model.master.Company;
import org.zama.examples.multitenant.model.master.Role;
import org.zama.examples.multitenant.model.master.User;
import org.zama.examples.multitenant.repository.master.CompanyRepository;
import org.zama.examples.multitenant.repository.master.RoleRepository;
import org.zama.examples.multitenant.repository.master.UserRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * UserService.
 *
 * @author Zakir Magdum
 */
@Service
@Transactional("masterTransactionManager")
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

    public Role saveRole(String name) {
        Role role = new Role();
        role.setName(name);
        role.setDescription(name);
        return saveRole(role);
    }

    public Role saveRole(Role role) {
        Optional<Role> roleOption = roleRepository.findOneByName(role.getName());
        return roleOption.isPresent() ? roleRepository.save(roleOption.get().merge(role)) :
                roleRepository.save(role);
    }

    public void removeRole(Role role) {
        roleRepository.delete(role);
    }

    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    public User registerUser(User user) {
        Optional<User> exists = userRepository.findOneByName(user.getName());
        return exists.isPresent() ? userRepository.save(exists.get().merge(user))
                : userRepository.save(user);
    }

    public void removeUser(String userName) {
        Optional<User> exists = userRepository.findOneByName(userName);
        if (exists.isPresent()) {
            removeUser(exists.get());
        }
    }

    public void removeUser(User user) {
        userRepository.delete(user);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }


}
