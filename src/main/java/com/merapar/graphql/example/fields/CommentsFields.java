package com.merapar.graphql.example.fields;

import com.merapar.graphql.GraphQlFields;
import com.merapar.graphql.example.datafetcher.CommentDataFetcher;
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
public class CommentsFields implements GraphQlFields {
    
    @Autowired
    private CommentDataFetcher commentDataFetcher;

    @Getter
    private List<GraphQLFieldDefinition> queryFields;

    @Getter
    private List<GraphQLFieldDefinition> mutationFields;

    @Getter
    private GraphQLObjectType commentType;

    private GraphQLInputObjectType filterCommentInputType;
    private GraphQLInputObjectType addCommentInputType;
    private GraphQLInputObjectType updateCommentInputType;
    private GraphQLInputObjectType deleteCommentInputType;

    private GraphQLFieldDefinition commentsField;
    private GraphQLFieldDefinition addCommentField;
    private GraphQLFieldDefinition updateCommentField;
    private GraphQLFieldDefinition deleteCommentField;

    @PostConstruct
    public void postConstruct() {
        createTypes();
        createFields();
        queryFields = Collections.singletonList(commentsField);
        mutationFields = Arrays.asList(addCommentField, updateCommentField, deleteCommentField);
    }

    private void createTypes() {
        commentType = newObject().name("comment").description("An comment")
            .field(newFieldDefinition().name("id").description("The id").type(GraphQLLong).build())
            .field(newFieldDefinition().name("author").description("The author").type(GraphQLString).build())
            .field(newFieldDefinition().name("comment").description("The comment").type(GraphQLString).build())
            .field(newFieldDefinition().name("blogEntryId").description("The blog entry id").type(GraphQLLong).build())
            .build();

        filterCommentInputType = newInputObject().name("filterCommentInput")
            .field(newInputObjectField().name("id").type(GraphQLLong).build())
            .build();

        addCommentInputType = newInputObject().name("addCommentInput")
            .field(newInputObjectField().name("id").type(new GraphQLNonNull(GraphQLLong)).build())
            .field(newInputObjectField().name("author").type(new GraphQLNonNull(Scalars.GraphQLString)).build())
            .field(newInputObjectField().name("comment").type(new GraphQLNonNull(Scalars.GraphQLString)).build())
            .field(newInputObjectField().name("blogEntryId").type(new GraphQLNonNull(GraphQLLong)).build())
            .build();

        updateCommentInputType = newInputObject().name("updateCommentInput")
            .field(newInputObjectField().name("id").type(new GraphQLNonNull(GraphQLLong)).build())
            .field(newInputObjectField().name("author").type(GraphQLString).build())
            .field(newInputObjectField().name("comment").type(new GraphQLNonNull(Scalars.GraphQLString)).build())
            .field(newInputObjectField().name("blogEntryId").type(new GraphQLNonNull(GraphQLLong)).build())
            .build();

        deleteCommentInputType = newInputObject().name("deleteCommentInput")
            .field(newInputObjectField().name("id").type(new GraphQLNonNull(GraphQLLong)).build())
            .build();
    }

    private void createFields() {
        commentsField = newFieldDefinition()
            .name("comments").description("Provide an overview of all comments")
            .type(new GraphQLList(commentType))
            .argument(newArgument().name(FILTER).type(filterCommentInputType).build())
            .dataFetcher(environment -> commentDataFetcher.getCommentsByFilter(getFilterMap(environment)))
            .build();

        addCommentField = newFieldDefinition()
            .name("addComment").description("Add new comment")
            .type(commentType)
            .argument(newArgument().name(INPUT).type(new GraphQLNonNull(addCommentInputType)).build())
            .dataFetcher(environment -> commentDataFetcher.addComment(getInputMap(environment)))
            .build();

        updateCommentField = newFieldDefinition()
            .name("updateComment").description("Update existing comment")
            .type(commentType)
            .argument(newArgument().name(INPUT).type(new GraphQLNonNull(updateCommentInputType)).build())
            .dataFetcher(environment -> commentDataFetcher.updateComment(getInputMap(environment)))
            .build();

        deleteCommentField = newFieldDefinition()
            .name("deleteComment").description("Delete existing comment")
            .type(commentType)
            .argument(newArgument().name(INPUT).type(new GraphQLNonNull(deleteCommentInputType)).build())
            .dataFetcher(environment -> commentDataFetcher.deleteComment(getInputMap(environment)))
            .build();
    }

}
