package org.zama.sample.graphql.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graphql.annotations.GraphQLField;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Department.
 */
@Entity
@Table(name = "department")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GraphQLField
    private Long id;

    @NotNull
    @Column(name = "department_name", nullable = false)
    @GraphQLField
    private String name;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @GraphQLField
    private Set<SalesPerson> salesPersons = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Department departmentName(String departmentName) {
        this.name = departmentName;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SalesPerson> getSalesPersons() {
        return salesPersons;
    }

    public Department departments(Set<SalesPerson> salesPeople) {
        this.salesPersons = salesPeople;
        return this;
    }

    public Department addSalesPerson(SalesPerson salesPerson) {
        this.salesPersons.add(salesPerson);
        salesPerson.setDepartment(this);
        return this;
    }

    public Department removeSalesPerson(SalesPerson salesPerson) {
        this.salesPersons.remove(salesPerson);
        salesPerson.setDepartment(null);
        return this;
    }

    public void setSalesPersons(Set<SalesPerson> salesPeople) {
        this.salesPersons = salesPeople;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Department department = (Department) o;
        if (department.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, department.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Department{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }

    public static final class Builder {
        private Long id;
        private String departmentName;
        private Set<SalesPerson> departments = new HashSet<>();

        private Builder() {
        }

        public static Builder aDepartment() {
            return new Builder();
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder departmentName(String departmentName) {
            this.departmentName = departmentName;
            return this;
        }

        public Builder departments(Set<SalesPerson> departments) {
            this.departments = departments;
            return this;
        }

        public Department build() {
            Department department = new Department();
            department.setId(id);
            department.setName(departmentName);
            department.setSalesPersons(departments);
            return department;
        }
    }
}
