package org.zama.sample.graphql.service.mapper;

import org.zama.sample.graphql.domain.*;
import org.zama.sample.graphql.service.dto.CustomerDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Customer and its DTO CustomerDTO.
 */
@Mapper(componentModel = "spring", uses = {LocationMapper.class, })
public interface CustomerMapper {

    @Mapping(source = "address.id", target = "addressId")
    CustomerDTO customerToCustomerDTO(Customer customer);

    List<CustomerDTO> customersToCustomerDTOs(List<Customer> customers);

    @Mapping(source = "addressId", target = "address")
    @Mapping(target = "orders", ignore = true)
    Customer customerDTOToCustomer(CustomerDTO customerDTO);

    List<Customer> customerDTOsToCustomers(List<CustomerDTO> customerDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default Customer customerFromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
    

}
