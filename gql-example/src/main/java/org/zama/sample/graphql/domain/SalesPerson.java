package org.zama.sample.graphql.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import graphql.annotations.GraphQLField;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SalesPerson.
 */
@Entity
@Table(name = "sales_person")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SalesPerson implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GraphQLField
    private Long id;

    @Column(name = "first_name")
    @GraphQLField
    private String firstName;

    @Column(name = "last_name")
    @GraphQLField
    private String lastName;

    @Column(name = "email")
    @GraphQLField
    private String email;

    @Column(name = "phone_number")
    @GraphQLField
    private String phoneNumber;

    @Column(name = "hire_date")
    @GraphQLField
    private ZonedDateTime hireDate;

    @Column(name = "salary")
    @GraphQLField
    private Long salary;

    @Column(name = "commission_pct")
    @GraphQLField
    private Long commissionPct;

    @ManyToOne
    @GraphQLField
    private Department department;

    @OneToMany(mappedBy = "salesPerson")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<CustomerOrder> salesPeople = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public SalesPerson firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public SalesPerson lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public SalesPerson email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public SalesPerson phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public ZonedDateTime getHireDate() {
        return hireDate;
    }

    public SalesPerson hireDate(ZonedDateTime hireDate) {
        this.hireDate = hireDate;
        return this;
    }

    public void setHireDate(ZonedDateTime hireDate) {
        this.hireDate = hireDate;
    }

    public Long getSalary() {
        return salary;
    }

    public SalesPerson salary(Long salary) {
        this.salary = salary;
        return this;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Long getCommissionPct() {
        return commissionPct;
    }

    public SalesPerson commissionPct(Long commissionPct) {
        this.commissionPct = commissionPct;
        return this;
    }

    public void setCommissionPct(Long commissionPct) {
        this.commissionPct = commissionPct;
    }

    public Department getDepartment() {
        return department;
    }

    public SalesPerson department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Set<CustomerOrder> getSalesPeople() {
        return salesPeople;
    }

    public SalesPerson salesPeople(Set<CustomerOrder> customerOrders) {
        this.salesPeople = customerOrders;
        return this;
    }

    public SalesPerson addSalesPerson(CustomerOrder customerOrder) {
        this.salesPeople.add(customerOrder);
        customerOrder.setSalesPerson(this);
        return this;
    }

    public SalesPerson removeSalesPerson(CustomerOrder customerOrder) {
        this.salesPeople.remove(customerOrder);
        customerOrder.setSalesPerson(null);
        return this;
    }

    public void setSalesPeople(Set<CustomerOrder> customerOrders) {
        this.salesPeople = customerOrders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SalesPerson salesPerson = (SalesPerson) o;
        if (salesPerson.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, salesPerson.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SalesPerson{" +
            "id=" + id +
            ", firstName='" + firstName + "'" +
            ", lastName='" + lastName + "'" +
            ", email='" + email + "'" +
            ", phoneNumber='" + phoneNumber + "'" +
            ", hireDate='" + hireDate + "'" +
            ", salary='" + salary + "'" +
            ", commissionPct='" + commissionPct + "'" +
            '}';
    }
}
