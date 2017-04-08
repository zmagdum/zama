package org.zama.sample.graphql.repository;

import org.zama.sample.graphql.domain.CustomerOrder;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CustomerOrder entity.
 */
@SuppressWarnings("unused")
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder,Long> {

}
