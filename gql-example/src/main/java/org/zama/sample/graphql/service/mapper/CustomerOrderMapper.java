package org.zama.sample.graphql.service.mapper;

import org.zama.sample.graphql.domain.*;
import org.zama.sample.graphql.service.dto.CustomerOrderDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity CustomerOrder and its DTO CustomerOrderDTO.
 */
@Mapper(componentModel = "spring", uses = {SalesPersonMapper.class, CustomerMapper.class, })
public interface CustomerOrderMapper {

    @Mapping(source = "salesPerson.id", target = "salesPersonId")
    @Mapping(source = "customer.id", target = "customerId")
    CustomerOrderDTO customerOrderToCustomerOrderDTO(CustomerOrder customerOrder);

    List<CustomerOrderDTO> customerOrdersToCustomerOrderDTOs(List<CustomerOrder> customerOrders);

    @Mapping(source = "salesPersonId", target = "salesPerson")
    @Mapping(target = "items", ignore = true)
    @Mapping(source = "customerId", target = "customer")
    CustomerOrder customerOrderDTOToCustomerOrder(CustomerOrderDTO customerOrderDTO);

    List<CustomerOrder> customerOrderDTOsToCustomerOrders(List<CustomerOrderDTO> customerOrderDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default CustomerOrder customerOrderFromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setId(id);
        return customerOrder;
    }
    

}
