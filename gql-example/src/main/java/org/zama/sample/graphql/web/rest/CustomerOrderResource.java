package org.zama.sample.graphql.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.zama.sample.graphql.domain.CustomerOrder;

import org.zama.sample.graphql.repository.CustomerOrderRepository;
import org.zama.sample.graphql.web.rest.util.HeaderUtil;
import org.zama.sample.graphql.service.dto.CustomerOrderDTO;
import org.zama.sample.graphql.service.mapper.CustomerOrderMapper;
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
 * REST controller for managing CustomerOrder.
 */
@RestController
@RequestMapping("/api")
public class CustomerOrderResource {

    private final Logger log = LoggerFactory.getLogger(CustomerOrderResource.class);

    private static final String ENTITY_NAME = "customerOrder";
        
    private final CustomerOrderRepository customerOrderRepository;

    private final CustomerOrderMapper customerOrderMapper;

    public CustomerOrderResource(CustomerOrderRepository customerOrderRepository, CustomerOrderMapper customerOrderMapper) {
        this.customerOrderRepository = customerOrderRepository;
        this.customerOrderMapper = customerOrderMapper;
    }

    /**
     * POST  /customer-orders : Create a new customerOrder.
     *
     * @param customerOrderDTO the customerOrderDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerOrderDTO, or with status 400 (Bad Request) if the customerOrder has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customer-orders")
    @Timed
    public ResponseEntity<CustomerOrderDTO> createCustomerOrder(@RequestBody CustomerOrderDTO customerOrderDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerOrder : {}", customerOrderDTO);
        if (customerOrderDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new customerOrder cannot already have an ID")).body(null);
        }
        CustomerOrder customerOrder = customerOrderMapper.customerOrderDTOToCustomerOrder(customerOrderDTO);
        customerOrder = customerOrderRepository.save(customerOrder);
        CustomerOrderDTO result = customerOrderMapper.customerOrderToCustomerOrderDTO(customerOrder);
        return ResponseEntity.created(new URI("/api/customer-orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customer-orders : Updates an existing customerOrder.
     *
     * @param customerOrderDTO the customerOrderDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerOrderDTO,
     * or with status 400 (Bad Request) if the customerOrderDTO is not valid,
     * or with status 500 (Internal Server Error) if the customerOrderDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customer-orders")
    @Timed
    public ResponseEntity<CustomerOrderDTO> updateCustomerOrder(@RequestBody CustomerOrderDTO customerOrderDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerOrder : {}", customerOrderDTO);
        if (customerOrderDTO.getId() == null) {
            return createCustomerOrder(customerOrderDTO);
        }
        CustomerOrder customerOrder = customerOrderMapper.customerOrderDTOToCustomerOrder(customerOrderDTO);
        customerOrder = customerOrderRepository.save(customerOrder);
        CustomerOrderDTO result = customerOrderMapper.customerOrderToCustomerOrderDTO(customerOrder);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customerOrderDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer-orders : get all the customerOrders.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of customerOrders in body
     */
    @GetMapping("/customer-orders")
    @Timed
    public List<CustomerOrderDTO> getAllCustomerOrders() {
        log.debug("REST request to get all CustomerOrders");
        List<CustomerOrder> customerOrders = customerOrderRepository.findAll();
        return customerOrderMapper.customerOrdersToCustomerOrderDTOs(customerOrders);
    }

    /**
     * GET  /customer-orders/:id : get the "id" customerOrder.
     *
     * @param id the id of the customerOrderDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerOrderDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customer-orders/{id}")
    @Timed
    public ResponseEntity<CustomerOrderDTO> getCustomerOrder(@PathVariable Long id) {
        log.debug("REST request to get CustomerOrder : {}", id);
        CustomerOrder customerOrder = customerOrderRepository.findOne(id);
        CustomerOrderDTO customerOrderDTO = customerOrderMapper.customerOrderToCustomerOrderDTO(customerOrder);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(customerOrderDTO));
    }

    /**
     * DELETE  /customer-orders/:id : delete the "id" customerOrder.
     *
     * @param id the id of the customerOrderDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customer-orders/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustomerOrder(@PathVariable Long id) {
        log.debug("REST request to delete CustomerOrder : {}", id);
        customerOrderRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
