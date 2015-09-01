package org.zama.examples.liquibase.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Role.
 *
 * @author Zakir Magdum
 */
@Entity
public @Data class Role extends BaseObject {
    @Column(length = 64)
    private String Description;
}
