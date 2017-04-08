package org.zama.sample.graphql.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.zama.sample.graphql.domain.SalesPerson;

import org.zama.sample.graphql.repository.SalesPersonRepository;
import org.zama.sample.graphql.web.rest.util.HeaderUtil;
import org.zama.sample.graphql.service.dto.SalesPersonDTO;
import org.zama.sample.graphql.service.mapper.SalesPersonMapper;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing SalesPerson.
 */
@RestController
@RequestMapping("/api")
public class SalesPersonResource {

    private final Logger log = LoggerFactory.getLogger(SalesPersonResource.class);

    private static final String ENTITY_NAME = "salesPerson";
        
    private final SalesPersonRepository salesPersonRepository;

    private final SalesPersonMapper salesPersonMapper;

    public SalesPersonResource(SalesPersonRepository salesPersonRepository, SalesPersonMapper salesPersonMapper) {
        this.salesPersonRepository = salesPersonRepository;
        this.salesPersonMapper = salesPersonMapper;
    }

    /**
     * POST  /sales-people : Create a new salesPerson.
     *
     * @param salesPersonDTO the salesPersonDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new salesPersonDTO, or with status 400 (Bad Request) if the salesPerson has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sales-people")
    @Timed
    public ResponseEntity<SalesPersonDTO> createSalesPerson(@RequestBody SalesPersonDTO salesPersonDTO) throws URISyntaxException {
        log.debug("REST request to save SalesPerson : {}", salesPersonDTO);
        if (salesPersonDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new salesPerson cannot already have an ID")).body(null);
        }
        SalesPerson salesPerson = salesPersonMapper.salesPersonDTOToSalesPerson(salesPersonDTO);
        salesPerson = salesPersonRepository.save(salesPerson);
        SalesPersonDTO result = salesPersonMapper.salesPersonToSalesPersonDTO(salesPerson);
        return ResponseEntity.created(new URI("/api/sales-people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /sales-people : Updates an existing salesPerson.
     *
     * @param salesPersonDTO the salesPersonDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated salesPersonDTO,
     * or with status 400 (Bad Request) if the salesPersonDTO is not valid,
     * or with status 500 (Internal Server Error) if the salesPersonDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/sales-people")
    @Timed
    public ResponseEntity<SalesPersonDTO> updateSalesPerson(@RequestBody SalesPersonDTO salesPersonDTO) throws URISyntaxException {
        log.debug("REST request to update SalesPerson : {}", salesPersonDTO);
        if (salesPersonDTO.getId() == null) {
            return createSalesPerson(salesPersonDTO);
        }
        SalesPerson salesPerson = salesPersonMapper.salesPersonDTOToSalesPerson(salesPersonDTO);
        salesPerson = salesPersonRepository.save(salesPerson);
        SalesPersonDTO result = salesPersonMapper.salesPersonToSalesPersonDTO(salesPerson);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, salesPersonDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /sales-people : get all the salesPeople.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of salesPeople in body
     */
    @GetMapping("/sales-people")
    @Timed
    public List<SalesPersonDTO> getAllSalesPeople() {
        log.debug("REST request to get all SalesPeople");
        List<SalesPerson> salesPeople = salesPersonRepository.findAll();
        return salesPersonMapper.salesPeopleToSalesPersonDTOs(salesPeople);
    }

    /**
     * GET  /sales-people/:id : get the "id" salesPerson.
     *
     * @param id the id of the salesPersonDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the salesPersonDTO, or with status 404 (Not Found)
     */
    @GetMapping("/sales-people/{id}")
    @Timed
    public ResponseEntity<SalesPersonDTO> getSalesPerson(@PathVariable Long id) {
        log.debug("REST request to get SalesPerson : {}", id);
        SalesPerson salesPerson = salesPersonRepository.findOne(id);
        SalesPersonDTO salesPersonDTO = salesPersonMapper.salesPersonToSalesPersonDTO(salesPerson);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(salesPersonDTO));
    }

    /**
     * DELETE  /sales-people/:id : delete the "id" salesPerson.
     *
     * @param id the id of the salesPersonDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/sales-people/{id}")
    @Timed
    public ResponseEntity<Void> deleteSalesPerson(@PathVariable Long id) {
        log.debug("REST request to delete SalesPerson : {}", id);
        salesPersonRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
