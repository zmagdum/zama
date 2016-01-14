package org.zama.examples.multitenant.model.master;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.zama.examples.multitenant.model.BaseObject;

import javax.persistence.*;
import java.util.*;

/**
 * User.
 *
 * @author Zakir Magdum
 */
@Entity
public class User extends BaseObject implements UserDetails {
    @Column(length = 128, nullable = false)
    private String password;                    // required
    @Column(length = 128)
    private String passwordHint;
    @Column(length = 128, nullable = false)
    private String firstName;                   // required
    @Column(length = 128, nullable = false)
    private String lastName;                    // required
    @Column(length = 128, unique = true, nullable = false)
    private String email;                       // required; unique
    @Column(length = 128)
    private String phoneNumber;
    @Column
    private boolean enabled;
    @Column
    private boolean accountExpired;
    @Column
    private boolean accountLocked;
    @Column
    private boolean credentialsExpired;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "companyId", nullable = false)
    private Company company;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<Role> roles = new HashSet<Role>();

    public User merge(User user) {
        super.merge(user);
        this.password = user.password;
        this.passwordHint = user.passwordHint;
        this.firstName = user.firstName;
        this.lastName = user.lastName;
        this.email = user.email;
        this.phoneNumber = user.phoneNumber;
        this.enabled = user.enabled;
        this.accountExpired = user.accountExpired;
        this.accountLocked = user.accountLocked;
        this.credentialsExpired = user.credentialsExpired;
        return this;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordHint() {
        return passwordHint;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }

    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> rls = new ArrayList<>();
        for (Role role: roles) {
            rls.add(new SimpleGrantedAuthority(role.getName()));
        }
        return rls;
    }

    @Override
    public String getUsername() {
        return getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }
}
