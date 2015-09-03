package org.zama.examples.liquibase.model;

import lombok.Data;
import org.codehaus.jackson.annotate.JsonManagedReference;

import javax.persistence.*;
import java.util.Set;

/**
 * Company.
 *
 * @author Zakir Magdum
 */
@Entity
public @Data class Company extends BaseObject {
    @Column(length = 64)
    private String companyKey;
    @Column(length = 255)
    private String description;
    @Column(length = 255)
    private String address;
    @Column
    private boolean enabled;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private Set<User> users;
}
