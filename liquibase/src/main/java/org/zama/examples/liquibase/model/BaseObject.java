package org.zama.examples.liquibase.model;

import lombok.Data;
import org.hibernate.annotations.Type;

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
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private LocalDateTime created;

    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private LocalDateTime updated;

    @Column
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private LocalDateTime deleted;

}
