package org.zama.examples.multitenant.model.master;

import org.zama.examples.multitenant.model.BaseObject;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Role.
 *
 * @author Zakir Magdum
 */
@Entity
public class Role extends BaseObject {
    @Column(length = 64)
    private String description;

    public Role merge(Role role) {
        super.merge(role);
        this.description = role.description;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
