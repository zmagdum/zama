package org.zama.sample.graphql.fetchers;

/**
 * DepartmentDataFetcher.
 *
 * @author Zakir Magdum.
 */

import com.google.common.base.Optional;
import com.merapar.graphql.base.TypedValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.zama.sample.graphql.domain.Department;
import org.zama.sample.graphql.repository.DepartmentRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class DepartmentDataFetcher {
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getByFilter(TypedValueMap arguments) {
        Long id = arguments.get("id");
        if (id != null) {
            return Collections.singletonList(departmentRepository.findOne(id));
        } else {
            return departmentRepository.findAll();
        }
    }

    public Department add(TypedValueMap arguments) {
        Department dept = Department.Builder.aDepartment().departmentName(arguments.get("departmentName")).build();
        return departmentRepository.save(dept);
    }

    public Department update(TypedValueMap arguments) {
        Long id = arguments.get("id");
        String name = arguments.get("departmentName");
        if (!StringUtils.hasLength(name)) {
            throw new IllegalArgumentException("Department not specified");
        }
        if (id == null) {
            return add(arguments);
        }
        Department dept = Optional.fromNullable(departmentRepository.getOne(id)).or(
            Department.Builder.aDepartment()
            .id(id)
            .build());
        dept.setDepartmentName(name);
        return departmentRepository.save(dept);
    }

    public Department delete(TypedValueMap arguments) {
        Long id = arguments.get("id");
        Department dept = departmentRepository.getOne(id);
        departmentRepository.delete(dept);
        return dept;
    }

}
