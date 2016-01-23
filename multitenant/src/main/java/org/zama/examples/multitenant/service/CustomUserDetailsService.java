package org.zama.examples.multitenant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zama.examples.multitenant.model.master.Company;
import org.zama.examples.multitenant.model.master.User;
import org.zama.examples.multitenant.repository.master.CompanyRepository;
import org.zama.examples.multitenant.repository.master.UserRepository;

import java.util.Optional;

/**
 * CustomUserDetailsService.
 *
 * @author Zakir Magdum
 */
@Service
@Transactional(value="masterTransactionManager", readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Autowired
    private UserRepository userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOGGER.trace("Looking for user for {}", username);
        try {
            Optional<User> user = userDao.findOneByName(username);
            if (!user.isPresent()) {
                LOGGER.info("USER NOT PRESENT for {} {}", username, user);
                throw new UsernameNotFoundException("user not found");
            }
            LOGGER.trace("Found user for {} {}", username, user);
            return user.get();
        } catch (Exception e) {
            LOGGER.error("Error loading user {}", username, e);
        }
        return null;
    }

}
