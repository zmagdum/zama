package org.zama.sample.graphql.fields;

import com.merapar.graphql.GraphQlProperties;
import com.merapar.graphql.base.TypedValueMap;
import com.merapar.graphql.executor.GraphQlExecutorProperties;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.Scalars;
import graphql.execution.ExecutorServiceExecutionStrategy;
import graphql.schema.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.zama.sample.graphql.fetchers.DepartmentDataFetcher;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.merapar.graphql.base.GraphQlFieldsHelper.getFilterMap;
import static com.merapar.graphql.base.GraphQlFieldsHelper.getInputMap;
import static graphql.Scalars.GraphQLInt;
import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static graphql.schema.GraphQLObjectType.newObject;

/**
 * GraphQLExecutor.
 *
 * @author Zakir Magdum
 */
@Component
public class GraphQLExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphQLExecutor.class);
    private GraphQL graphQL;
    @Autowired
    private DepartmentDataFetcher deptDataFetcher;

    private GraphQLObjectType deptType;

    private GraphQLInputObjectType pageInputType;
    private GraphQLObjectType pageInfoType;

    private GraphQLInputObjectType saveDepartmentInputType;
    private GraphQLInputObjectType updateDepartmentInputType;
    private GraphQLInputObjectType deleteDepartmentInputType;

    private GraphQLInputObjectType filterDepartmentInputType;

    private GraphQLFieldDefinition deptsField;
    private GraphQLFieldDefinition saveDepartmentField;
    private GraphQLFieldDefinition updateDepartmentField;
    private GraphQLFieldDefinition deleteDepartmentField;
    private GraphQLSchema graphQLSchema;
    private GraphQlExecutorProperties processorProperties;
    private GraphQlProperties graphQlProperties;

    public GraphQLExecutor(GraphQlExecutorProperties processorProperties, GraphQlProperties graphQlProperties) {
        this.processorProperties = processorProperties;
        this.graphQlProperties = graphQlProperties;
    }


    @PostConstruct
    private void postConstruct() {
        createTypes();
        createFields();
        createSchema();
        this.graphQL = new GraphQL(this.graphQLSchema, this.createExecutionStrategy());
    }

    private ExecutorServiceExecutionStrategy createExecutionStrategy() {
        LinkedBlockingQueue queue = new LinkedBlockingQueue() {
            public boolean offer(Runnable e) {
                return false;
            }
        };
        return new ExecutorServiceExecutionStrategy(new ThreadPoolExecutor(this.processorProperties.getMinimumThreadPoolSize().intValue(),
            this.processorProperties.getMaximumThreadPoolSize().intValue(), (long)this.processorProperties.getKeepAliveTimeInSeconds().intValue(),
            TimeUnit.SECONDS, queue, new ThreadPoolExecutor.CallerRunsPolicy()));
    }



    public Object executeRequest(String request) {
        ExecutionResult executionResult = this.graphQL.execute(request);
        LinkedHashMap result = new LinkedHashMap();
        if(executionResult.getErrors().size() > 0) {
            result.put("errors", executionResult.getErrors());
            LOGGER.error("Errors: {}", executionResult.getErrors());
        }
        result.put("data", executionResult.getData());
        return result;

    }


    private void createTypes() {
        deptType = newObject().name("department").description("A department")
            .field(newFieldDefinition().name("id").description("The id").type(GraphQLLong).build())
            .field(newFieldDefinition().name("name").description("The name").type(GraphQLString).build())
            .build();

        saveDepartmentInputType = newInputObject().name("saveDepartmentInput")
            .field(newInputObjectField().name("id").type(new GraphQLNonNull(GraphQLLong)).build())
            .field(newInputObjectField().name("name").type(new GraphQLNonNull(Scalars.GraphQLString)).build())
            .build();

        deleteDepartmentInputType = newInputObject().name("deleteDepartmentInput")
            .field(newInputObjectField().name("id").type(new GraphQLNonNull(GraphQLLong)).build())
            .build();

        filterDepartmentInputType = newInputObject().name("filterDepartmentInput")
            .field(newInputObjectField().name("id").type(GraphQLLong).build())
            .build();

        pageInputType = newInputObject().name("page").description("A page")
            .field(newInputObjectField().name("number").description("The id").type(GraphQLInt).build())
            .field(newInputObjectField().name("size").description("The name").type(GraphQLInt).build())
            .field(newInputObjectField().name("sort").description("The name").type(GraphQLString).build())
            .field(newInputObjectField().name("command").description("The name").type(GraphQLString).build())
            .build();

        pageInfoType = newObject().name("pageInfo").description("A page")
            .field(newFieldDefinition().name("number").description("The id").type(GraphQLInt).build())
            .field(newFieldDefinition().name("size").description("The name").type(GraphQLInt).build())
            .field(newFieldDefinition().name("total").description("The name").type(GraphQLInt).build())
            .build();
    }

    private void createFields() {

        GraphQLObjectType deptOutputType = newObject().name("department").description("A department")
            .field(newFieldDefinition().name("departments").description("The results").type(new GraphQLList(deptType)).build())
            .field(newFieldDefinition().name("pageInfo").description("The name").type(pageInfoType).build())
            .build();

        deptsField = newFieldDefinition()
            .name("queryDepartment").description("Provide an overview of all departments")
            .type(deptOutputType)
            .argument(newArgument().name("id").description("The id").type(GraphQLLong).build())
            .argument(newArgument().name("name").description("The name").type(GraphQLString).build())
            .argument(newArgument().name("page").description("The page").type(pageInputType).build())
            .dataFetcher(environment -> deptDataFetcher.getByFilter(getArgumentsTypedMap(environment)))
            .build();

        saveDepartmentField = newFieldDefinition()
            .name("saveDepartment").description("Add new/save department")
            .type(deptType)
            .argument(newArgument().name("id").description("The id").type(GraphQLLong).build())
            .argument(newArgument().name("name").description("The name").type(GraphQLString).build())
            .dataFetcher(environment -> deptDataFetcher.update(getArgumentsTypedMap(environment)))
            .build();

        deleteDepartmentField = newFieldDefinition()
            .name("deleteDepartment").description("Delete existing department")
            .type(deptType)
            .argument(newArgument().name("id").description("The id").type(GraphQLLong).build())
            .dataFetcher(environment -> deptDataFetcher.delete(getArgumentsTypedMap(environment)))
            .build();

//        deptQueryType = newObject()
//            .name("queryDepartment").description("Provide an overview of all departments")
//            .field(
//                newFieldDefinition()
//                    .name("some name")
//                .type(new GraphQLList(deptType))
//                .argument(
//                    newArgument()
//                        .name("id")
//                        .description("The id")
//                        .type(GraphQLLong).build())
//                .argument(
//                    newArgument()
//                        .name("name")
//                        .description("The name")
//                        .type(GraphQLString).build())
//                .dataFetcher(environment -> deptDataFetcher.getByFilter(getFilterMap(environment)))
//            )
//            .build();

    }

    private void createSchema() {
        GraphQLObjectType.Builder queryBuilder = newObject().name(graphQlProperties.getRootQueryName());
        GraphQLObjectType.Builder mutationBuilder = newObject().name(graphQlProperties.getRootMutationName());

        if (StringUtils.hasText(graphQlProperties.getRootQueryDescription())) {
            queryBuilder = queryBuilder.description(graphQlProperties.getRootQueryDescription());
        }

        if (StringUtils.hasText(graphQlProperties.getRootMutationDescription())) {
            mutationBuilder = mutationBuilder.description(graphQlProperties.getRootMutationDescription());
        }
//        queryBuilder = queryBuilder.fields(Collections.singletonList(deptsField));
        queryBuilder = queryBuilder.fields(Arrays.asList(deptsField, saveDepartmentField, deleteDepartmentField));
        mutationBuilder = mutationBuilder.fields(Arrays.asList(saveDepartmentField, deleteDepartmentField));


        this.graphQLSchema = GraphQLSchema.newSchema().query(queryBuilder.build()).mutation(mutationBuilder.build()).build();
        System.out.println(this.graphQLSchema.getAllTypesAsList());
    }

    public static TypedValueMap getArgumentsTypedMap(DataFetchingEnvironment environment) {
        return new TypedValueMap(environment.getArguments());
    }

}
