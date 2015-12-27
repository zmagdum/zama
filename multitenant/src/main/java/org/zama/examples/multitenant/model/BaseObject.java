package org.zama.examples.multitenant.model;

import lombok.Data;
import org.zama.examples.multitenant.util.LocalDateTimePersistenceConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

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
