package org.zama.examples.multitenant.model;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonBackReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * User.
 *
 * @author Zakir Magdum
 */
@Entity
public @Data class User extends BaseObject {
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
}
