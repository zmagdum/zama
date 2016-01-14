package org.zama.examples.multitenant.add;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * CurrentTenantIdentifierResolverImpl.
 *
 * @author Zakir Magdum
 */
@Component
public class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver {
     @Override
        public String resolveCurrentTenantIdentifier() {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes != null) {
                String identifier = (String) requestAttributes.getAttribute("CURRENT_TENANT_IDENTIFIER",
                        RequestAttributes.SCOPE_REQUEST);
                if (identifier != null) {
                    return identifier;
                }
            }
            throw new IllegalArgumentException("Current Tenant Identifier can not be determined");
        }

        @Override
        public boolean validateExistingCurrentSessions() {
            return true;
        }
    }
