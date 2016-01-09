package org.zama.examples.multitenant.util;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * BCryptPasswordEncoder.
 *
 * @author Zakir Magdum
 */
public class BCryptPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return BCrypt.hashpw(charSequence.toString(), BCrypt.gensalt());
    }

    @Override
    public boolean matches(CharSequence rawPass, String encPass) {
        return BCrypt.checkpw(rawPass.toString(), encPass);
    }
}
