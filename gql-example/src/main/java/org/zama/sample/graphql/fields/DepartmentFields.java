package org.zama.sample.graphql.fields;

import com.merapar.graphql.GraphQlFields;
import graphql.Scalars;
import graphql.schema.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zama.sample.graphql.fetchers.DepartmentDataFetcher;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.merapar.graphql.base.GraphQlFieldsHelper.*;
import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static graphql.schema.GraphQLObjectType.newObject;

/**
 * DepartmentFields.
 *
 * @author Zakir Magdum.
 */
@Component
public class DepartmentFields implements GraphQlFields {

    @Autowired
    private DepartmentDataFetcher deptDataFetcher;

    private GraphQLObjectType deptType;

    private GraphQLInputObjectType addDepartmentInputType;
    private GraphQLInputObjectType updateDepartmentInputType;
    private GraphQLInputObjectType deleteDepartmentInputType;

    private GraphQLInputObjectType filterDepartmentInputType;

    private GraphQLFieldDefinition deptsField;
    private GraphQLFieldDefinition addDepartmentField;
    private GraphQLFieldDefinition updateDepartmentField;
    private GraphQLFieldDefinition deleteDepartmentField;

    private List<GraphQLFieldDefinition> queryFields;

    private List<GraphQLFieldDefinition> mutationFields;

    @PostConstruct
    public void postConstruct() {
        createTypes();
        createFields();
        queryFields = Collections.singletonList(deptsField);
        mutationFields = Arrays.asList(addDepartmentField, updateDepartmentField, deleteDepartmentField);
    }

    private void createTypes() {
        deptType = newObject().name("department").description("A department")
            .field(newFieldDefinition().name("id").description("The id").type(GraphQLLong).build())
            .field(newFieldDefinition().name("departmentName").description("The name").type(GraphQLString).build())
            .build();

        addDepartmentInputType = newInputObject().name("addDepartmentInput")
            .field(newInputObjectField().name("departmentName").type(new GraphQLNonNull(Scalars.GraphQLString)).build())
            .build();

        updateDepartmentInputType = newInputObject().name("updateDepartmentInput")
            .field(newInputObjectField().name("id").type(new GraphQLNonNull(GraphQLLong)).build())
            .field(newInputObjectField().name("departmentName").type(GraphQLString).build())
            .build();

        deleteDepartmentInputType = newInputObject().name("deleteDepartmentInput")
            .field(newInputObjectField().name("departmentName").type(new GraphQLNonNull(GraphQLLong)).build())
            .build();

        filterDepartmentInputType = newInputObject().name("filterDepartmentInput")
            .field(newInputObjectField().name("id").type(GraphQLLong).build())
            .build();
    }

    private void createFields() {
        deptsField = newFieldDefinition()
            .name("queryDepartment").description("Provide an overview of all departments")
            .type(new GraphQLList(deptType))
            .argument(newArgument().name(FILTER).type(filterDepartmentInputType).build())
            .dataFetcher(environment -> deptDataFetcher.getByFilter(getFilterMap(environment)))
            .build();

        addDepartmentField = newFieldDefinition()
            .name("addDepartment").description("Add new department")
            .type(deptType)
            .argument(newArgument().name(INPUT).type(new GraphQLNonNull(addDepartmentInputType)).build())
            .dataFetcher(environment -> deptDataFetcher.add(getInputMap(environment)))
            .build();

        updateDepartmentField = newFieldDefinition()
            .name("updateDepartment").description("Update existing department")
            .type(deptType)
            .argument(newArgument().name(INPUT).type(new GraphQLNonNull(updateDepartmentInputType)).build())
            .dataFetcher(environment -> deptDataFetcher.update(getInputMap(environment)))
            .build();

        deleteDepartmentField = newFieldDefinition()
            .name("deleteDepartment").description("Delete existing department")
            .type(deptType)
            .argument(newArgument().name(INPUT).type(new GraphQLNonNull(deleteDepartmentInputType)).build())
            .dataFetcher(environment -> deptDataFetcher.delete(getInputMap(environment)))
            .build();
    }

    @Override
    public List<GraphQLFieldDefinition> getQueryFields() {
        return queryFields;
    }

    @Override
    public List<GraphQLFieldDefinition> getMutationFields() {
        return mutationFields;
    }

    public GraphQLObjectType getDeptType() {
        return deptType;
    }
}
