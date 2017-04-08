package org.zama.sample.graphql.service.mapper;

import org.zama.sample.graphql.domain.*;
import org.zama.sample.graphql.service.dto.SalesPersonDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity SalesPerson and its DTO SalesPersonDTO.
 */
@Mapper(componentModel = "spring", uses = {DepartmentMapper.class, })
public interface SalesPersonMapper {

    @Mapping(source = "department.id", target = "departmentId")
    SalesPersonDTO salesPersonToSalesPersonDTO(SalesPerson salesPerson);

    List<SalesPersonDTO> salesPeopleToSalesPersonDTOs(List<SalesPerson> salesPeople);

    @Mapping(source = "departmentId", target = "department")
    @Mapping(target = "salesPeople", ignore = true)
    SalesPerson salesPersonDTOToSalesPerson(SalesPersonDTO salesPersonDTO);

    List<SalesPerson> salesPersonDTOsToSalesPeople(List<SalesPersonDTO> salesPersonDTOs);
    /**
     * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
     * creating a new attribute to know if the entity has any relationship from some other entity
     *
     * @param id id of the entity
     * @return the entity instance
     */
     
    default SalesPerson salesPersonFromId(Long id) {
        if (id == null) {
            return null;
        }
        SalesPerson salesPerson = new SalesPerson();
        salesPerson.setId(id);
        return salesPerson;
    }
    

}
