package org.zama.sample.graphql.fetchers;

/**
 * DepartmentDataFetcher.
 *
 * @author Zakir Magdum.
 */

import com.google.common.base.Optional;
import com.merapar.graphql.base.TypedValueMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public DepartmentQueryResult getByFilter(TypedValueMap arguments) {
        Long id = arguments.get("id");
        if (id != null) {
            return new DepartmentQueryResult(Collections.singletonList(departmentRepository.findOne(id)), new PageInfo(1));
        } else {
            int pageNumber = 0, pageSize = 25;
            TypedValueMap page = arguments.get("page");
            if (page != null) {
                Integer num = page.get("number");
                if (num != null) {
                    pageNumber = num;
                }
                num = page.get("size");
                if (num != null) {
                    pageSize = num;
                }
            }
            // TODO: sorting
            PageRequest pr = new PageRequest(pageNumber, pageSize);
            Page<Department> pd = departmentRepository.findAll(pr);
            return new DepartmentQueryResult(pd, pr);
        }
    }

    public Department add(TypedValueMap arguments) {
        Department dept = Department.Builder.aDepartment().departmentName(arguments.get("name")).build();
        return departmentRepository.save(dept);
    }

    public Department update(TypedValueMap arguments) {
        Long id = arguments.get("id");
        String name = arguments.get("name");
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
        dept.setName(name);
        return departmentRepository.save(dept);
    }

    public Department delete(TypedValueMap arguments) {
        Long id = arguments.get("id");
        Department dept = departmentRepository.getOne(id);
        departmentRepository.delete(dept);
        return dept;
    }
}
