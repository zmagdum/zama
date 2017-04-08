package org.zama.sample.graphql.repository;

import org.zama.sample.graphql.domain.SalesPerson;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SalesPerson entity.
 */
@SuppressWarnings("unused")
public interface SalesPersonRepository extends JpaRepository<SalesPerson,Long> {

}
