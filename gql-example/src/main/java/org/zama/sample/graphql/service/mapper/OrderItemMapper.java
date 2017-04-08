package org.zama.sample.graphql.service.mapper;

import org.zama.sample.graphql.domain.*;
import org.zama.sample.graphql.service.dto.OrderItemDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity OrderItem and its DTO OrderItemDTO.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class, CustomerOrderMapper.class, })
public interface OrderItemMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "customerOrder.id", target = "customerOrderId")
    OrderItemDTO orderItemToOrderItemDTO(OrderItem orderItem);

    List<OrderItemDTO> orderItemsToOrderItemDTOs(List<OrderItem> orderItems);

    @Mapping(source = "productId", target = "product")
    @Mapping(source = "customerOrderId", target = "customerOrder")
    OrderItem orderItemDTOToOrderItem(OrderItemDTO orderItemDTO);

    List<OrderItem> orderItemDTOsToOrderItems(List<OrderItemDTO> orderItemDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default OrderItem orderItemFromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setId(id);
        return orderItem;
    }
    

}
