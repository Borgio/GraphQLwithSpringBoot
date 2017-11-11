package com.merapar.graphql.example.fields;

import com.merapar.graphql.GraphQlFields;
import com.merapar.graphql.example.datafetcher.AuthorDataFetcher;
import graphql.Scalars;
import graphql.schema.GraphQLFieldDefinition;
import graphql.schema.GraphQLInputObjectType;
import graphql.schema.GraphQLList;
import graphql.schema.GraphQLNonNull;
import graphql.schema.GraphQLObjectType;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.merapar.graphql.base.GraphQlFieldsHelper.FILTER;
import static com.merapar.graphql.base.GraphQlFieldsHelper.INPUT;
import static com.merapar.graphql.base.GraphQlFieldsHelper.getFilterMap;
import static com.merapar.graphql.base.GraphQlFieldsHelper.getInputMap;
import static graphql.Scalars.GraphQLLong;
import static graphql.Scalars.GraphQLString;
import static graphql.schema.GraphQLArgument.newArgument;
import static graphql.schema.GraphQLFieldDefinition.newFieldDefinition;
import static graphql.schema.GraphQLInputObjectField.newInputObjectField;
import static graphql.schema.GraphQLInputObjectType.newInputObject;
import static graphql.schema.GraphQLObjectType.newObject;

@Component
public class AuthorFields implements GraphQlFields {

    @Autowired
    private AuthorDataFetcher authorDataFetcher;


    @Getter
    private List<GraphQLFieldDefinition> queryFields;

    @Getter
    private List<GraphQLFieldDefinition> mutationFields;

    @Getter
    private GraphQLObjectType authorType;

    private GraphQLInputObjectType filterAuthorInputType;
    private GraphQLInputObjectType addAuthorInputType;
    private GraphQLInputObjectType updateAuthorInputType;
    private GraphQLInputObjectType deleteAuthorInputType;

    private GraphQLFieldDefinition authorsField;
    private GraphQLFieldDefinition addAuthorField;
    private GraphQLFieldDefinition updateAuthorField;
    private GraphQLFieldDefinition deleteAuthorField;

    @PostConstruct
    public void postConstruct() {
        createTypes();
        createFields();
        queryFields = Collections.singletonList(authorsField);
        mutationFields = Arrays.asList(addAuthorField, updateAuthorField, deleteAuthorField);
    }

    private void createTypes() {
        authorType = newObject().name("author").description("An author")
            .field(newFieldDefinition().name("id").description("The id").type(GraphQLLong).build())
            .field(newFieldDefinition().name("name").description("The name").type(GraphQLString).build())
            .field(newFieldDefinition().name("bio").description("The biography").type(GraphQLString).build())
            .field(newFieldDefinition().name("email").description("The email address").type(GraphQLString).build())
            .build();

        filterAuthorInputType = newInputObject().name("filterAuthorInput")
            .field(newInputObjectField().name("id").type(GraphQLLong).build())
            .build();

        addAuthorInputType = newInputObject().name("addAuthorInput")
            .field(newInputObjectField().name("id").type(new GraphQLNonNull(GraphQLLong)).build())
            .field(newInputObjectField().name("name").type(new GraphQLNonNull(Scalars.GraphQLString)).build())
            .field(newInputObjectField().name("bio").type(new GraphQLNonNull(Scalars.GraphQLString)).build())
            .field(newInputObjectField().name("email").type(new GraphQLNonNull(Scalars.GraphQLString)).build())
            .build();

        updateAuthorInputType = newInputObject().name("updateAuthorInput")
            .field(newInputObjectField().name("id").type(new GraphQLNonNull(GraphQLLong)).build())
            .field(newInputObjectField().name("name").type(GraphQLString).build())
            .field(newInputObjectField().name("bio").type(new GraphQLNonNull(Scalars.GraphQLString)).build())
            .field(newInputObjectField().name("email").type(new GraphQLNonNull(Scalars.GraphQLString)).build())
            .build();

        deleteAuthorInputType = newInputObject().name("deleteAuthorInput")
            .field(newInputObjectField().name("id").type(new GraphQLNonNull(GraphQLLong)).build())
            .build();
    }

    private void createFields() {
        authorsField = newFieldDefinition()
            .name("authors").description("Provide an overview of all authors")
            .type(new GraphQLList(authorType))
            .argument(newArgument().name(FILTER).type(filterAuthorInputType).build())
            .dataFetcher(environment -> authorDataFetcher.getAuthorsByFilter(getFilterMap(environment)))
            .build();

        addAuthorField = newFieldDefinition()
            .name("addAuthor").description("Add new author")
            .type(authorType)
            .argument(newArgument().name(INPUT).type(new GraphQLNonNull(addAuthorInputType)).build())
            .dataFetcher(environment -> authorDataFetcher.addAuthor(getInputMap(environment)))
            .build();

        updateAuthorField = newFieldDefinition()
            .name("updateAuthor").description("Update existing author")
            .type(authorType)
            .argument(newArgument().name(INPUT).type(new GraphQLNonNull(updateAuthorInputType)).build())
            .dataFetcher(environment -> authorDataFetcher.updateAuthor(getInputMap(environment)))
            .build();

        deleteAuthorField = newFieldDefinition()
            .name("deleteAuthor").description("Delete existing author")
            .type(authorType)
            .argument(newArgument().name(INPUT).type(new GraphQLNonNull(deleteAuthorInputType)).build())
            .dataFetcher(environment -> authorDataFetcher.deleteAuthor(getInputMap(environment)))
            .build();
    }

}
