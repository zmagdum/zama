package org.zama.sample.graphql.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A CustomerOrder.
 */
@Entity
@Table(name = "customer_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomerOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_date")
    private ZonedDateTime orderDate;

    @Column(name = "comments")
    private String comments;

    @Column(name = "total_value")
    private Double totalValue;

    @ManyToOne
    private SalesPerson salesPerson;

    @OneToMany(mappedBy = "customerOrder")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<OrderItem> items = new HashSet<>();

    @ManyToOne
    private Customer customer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getOrderDate() {
        return orderDate;
    }

    public CustomerOrder orderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(ZonedDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public String getComments() {
        return comments;
    }

    public CustomerOrder comments(String comments) {
        this.comments = comments;
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public CustomerOrder totalValue(Double totalValue) {
        this.totalValue = totalValue;
        return this;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public SalesPerson getSalesPerson() {
        return salesPerson;
    }

    public CustomerOrder salesPerson(SalesPerson salesPerson) {
        this.salesPerson = salesPerson;
        return this;
    }

    public void setSalesPerson(SalesPerson salesPerson) {
        this.salesPerson = salesPerson;
    }

    public Set<OrderItem> getItems() {
        return items;
    }

    public CustomerOrder items(Set<OrderItem> orderItems) {
        this.items = orderItems;
        return this;
    }

    public CustomerOrder addItems(OrderItem orderItem) {
        this.items.add(orderItem);
        orderItem.setCustomerOrder(this);
        return this;
    }

    public CustomerOrder removeItems(OrderItem orderItem) {
        this.items.remove(orderItem);
        orderItem.setCustomerOrder(null);
        return this;
    }

    public void setItems(Set<OrderItem> orderItems) {
        this.items = orderItems;
    }

    public Customer getCustomer() {
        return customer;
    }

    public CustomerOrder customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomerOrder customerOrder = (CustomerOrder) o;
        if (customerOrder.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, customerOrder.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerOrder{" +
            "id=" + id +
            ", orderDate='" + orderDate + "'" +
            ", comments='" + comments + "'" +
            ", totalValue='" + totalValue + "'" +
            '}';
    }
}
