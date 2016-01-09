package org.zama.examples.multitenant.model;

import org.zama.examples.multitenant.util.LocalDateTimePersistenceConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * BaseObject.
 * @author Zakir Magdum
 */
@MappedSuperclass
public class BaseObject<T extends BaseObject> {

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseObject<?> that = (BaseObject<?>) o;

        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
