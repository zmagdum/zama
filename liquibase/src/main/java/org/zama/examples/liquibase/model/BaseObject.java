package org.zama.examples.liquibase.model;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.zama.examples.liquibase.util.LocalDateTimePersistenceConverter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * BaseObject.
 * @author Zakir Magdum
 */
@MappedSuperclass
public @Data class BaseObject<T extends BaseObject> {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false, length=128)
    private String name;

    @Column
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime created;

    @Column
    @Convert(converter = LocalDateTimePersistenceConverter.class)
    private LocalDateTime updated;

//    @Column
//    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
//    private LocalDateTime deleted;

    public T merge(T other) {
        // do not copy id so as not to confuse hibernate
        this.name = other.getName();
        this.created = other.getCreated();
        this.updated = other.getUpdated();

        return (T) this;
    }

}
