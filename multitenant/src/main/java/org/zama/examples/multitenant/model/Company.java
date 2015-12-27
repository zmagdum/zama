package org.zama.examples.multitenant.model;

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
public @Data class Company extends BaseObject<Company> {
    @Column(length = 64, unique = true, nullable = false)
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

    public Company merge(Company company) {
        super.merge(company);
        this.companyKey = company.companyKey;
        this.description = company.description;
        this.address = company.address;
        this.enabled = company.enabled;
        // TODO: sync users in case they exists
        return this;
    }
}
