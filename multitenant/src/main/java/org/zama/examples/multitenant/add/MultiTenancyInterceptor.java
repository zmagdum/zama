package org.zama.examples.multitenant.add;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.zama.examples.multitenant.model.master.User;
import org.zama.examples.multitenant.repository.master.UserRepository;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * MultiTenancyInterceptor.
 *
 * @author Zakir Magdum
 */
@Component
public class MultiTenancyInterceptor extends HandlerInterceptorAdapter {
    @Inject
    private UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        if (req.getUserPrincipal() != null) {
            Optional<User> uo = userRepository.findOneByName(req.getUserPrincipal().getName());
            if (uo.isPresent()) {
                req.setAttribute("CURRENT_TENANT_IDENTIFIER", uo.get().getCompany().getCompanyKey());
            }
        }
        return true;
    }
}