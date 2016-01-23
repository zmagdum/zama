package org.zama.examples.multitenant.confighelper;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.zama.examples.multitenant.model.master.User;
import org.zama.examples.multitenant.repository.master.UserRepository;
import org.zama.examples.multitenant.util.Constants;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * TenantIdentifierInterceptorAdapter.
 *
 * @author Zakir Magdum
 */
@Component
public class TenantIdentifierInterceptorAdapter extends HandlerInterceptorAdapter {
    @Inject
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        if (req.getUserPrincipal() != null) {
            Optional<User> uo = userRepository.findOneByName(req.getUserPrincipal().getName());
            if (uo.isPresent()) {
                // Set the company key as tenant identifier
                req.setAttribute(Constants.CURRENT_TENANT_IDENTIFIER, uo.get().getCompany().getCompanyKey());
            }
        }
        return true;
    }
}