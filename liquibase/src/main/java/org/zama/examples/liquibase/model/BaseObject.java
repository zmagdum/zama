package org.zama.examples.liquibase.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * BaseObject.
 * @author Zakir Magdum
 */
@MappedSuperclass
public @Data class BaseObject {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false, length=128)
    private String name;

    @Column
    private LocalDateTime created;
    @Column
    private LocalDateTime updated;
    @Column
    private LocalDateTime deleted;

}
