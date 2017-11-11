package com.merapar.graphql.example.fields;

import com.merapar.graphql.GraphQlFields;
import com.merapar.graphql.example.datafetcher.AuthorDataFetcher;
import com.merapar.graphql.example.datafetcher.BlogEntryDataFetcher;
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
public class BlogEntryFields implements GraphQlFields {

    @Autowired
    private BlogEntryDataFetcher blogEntryDataFetcher;

    @Autowired
    private CommentDataFetcher commentDataFetcher;

    @Autowired
    private AuthorDataFetcher authorDataFetcher;

    @Autowired
    private AuthorFields authorFields;

    @Autowired
    private CommentsFields commentsFields;

    @Getter
    private List<GraphQLFieldDefinition> queryFields;

    @Getter
    private List<GraphQLFieldDefinition> mutationFields;

    private GraphQLObjectType blogEntryType;

    private GraphQLInputObjectType filterBlogEntryInputType;
    private GraphQLInputObjectType addBlogEntryInputType;
    private GraphQLInputObjectType updateBlogEntryInputType;
    private GraphQLInputObjectType deleteBlogEntryInputType;

    private GraphQLFieldDefinition blogEntriesField;
    private GraphQLFieldDefinition addBlogEntryField;
    private GraphQLFieldDefinition updateBlogEntryField;
    private GraphQLFieldDefinition deleteBlogEntryField;

    @PostConstruct
    public void postConstruct() {
        createTypes();
        createFields();
        queryFields = Collections.singletonList(blogEntriesField);
        mutationFields = Arrays.asList(addBlogEntryField, updateBlogEntryField, deleteBlogEntryField);
    }

    private void createTypes() {
        blogEntryType = newObject().name("blogEntry").description("An blogEntry")
            .field(newFieldDefinition().name("id").description("The id").type(GraphQLLong).build())
            .field(newFieldDefinition().name("title").description("The title").type(GraphQLString).build())
            .field(newFieldDefinition().name("author").description("The author")
                .type(authorFields.getAuthorType())
                .dataFetcher(blogEntryDataFetcher::getAuthorByAuthorId).build())
            .field(newFieldDefinition().name("comments").description("The comments")
                .type(new GraphQLList(commentsFields.getCommentType()))
                .dataFetcher(blogEntryDataFetcher::getCommentsByBlogEntryId).build())
            .build();

        filterBlogEntryInputType = newInputObject().name("filterBlogEntryInput")
            .field(newInputObjectField().name("id").type(GraphQLLong).build())
            .build();

        addBlogEntryInputType = newInputObject().name("addBlogEntryInput")
            .field(newInputObjectField().name("id").type(new GraphQLNonNull(GraphQLLong)).build())
            .field(newInputObjectField().name("author").type(new GraphQLNonNull(Scalars.GraphQLString)).build())
            .field(newInputObjectField().name("blogEntry").type(new GraphQLNonNull(Scalars.GraphQLString)).build())
            .field(newInputObjectField().name("blogEntryId").type(new GraphQLNonNull(GraphQLLong)).build())
            .build();

        updateBlogEntryInputType = newInputObject().name("updateBlogEntryInput")
            .field(newInputObjectField().name("id").type(new GraphQLNonNull(GraphQLLong)).build())
            .field(newInputObjectField().name("author").type(GraphQLString).build())
            .field(newInputObjectField().name("blogEntry").type(new GraphQLNonNull(Scalars.GraphQLString)).build())
            .field(newInputObjectField().name("blogEntryId").type(new GraphQLNonNull(GraphQLLong)).build())
            .build();

        deleteBlogEntryInputType = newInputObject().name("deleteBlogEntryInput")
            .field(newInputObjectField().name("id").type(new GraphQLNonNull(GraphQLLong)).build())
            .build();
    }

    private void createFields() {
        blogEntriesField = newFieldDefinition()
            .name("blogEntrys").description("Provide an overview of all blogEntrys")
            .type(new GraphQLList(blogEntryType))
            .argument(newArgument().name(FILTER).type(filterBlogEntryInputType).build())
            .dataFetcher(environment -> blogEntryDataFetcher.getBlogEntriesByFilter(getFilterMap(environment)))
            .build();

        addBlogEntryField = newFieldDefinition()
            .name("addBlogEntry").description("Add new blogEntry")
            .type(blogEntryType)
            .argument(newArgument().name(INPUT).type(new GraphQLNonNull(addBlogEntryInputType)).build())
            .dataFetcher(environment -> blogEntryDataFetcher.addBlogEntry(getInputMap(environment)))
            .build();

        updateBlogEntryField = newFieldDefinition()
            .name("updateBlogEntry").description("Update existing blogEntry")
            .type(blogEntryType)
            .argument(newArgument().name(INPUT).type(new GraphQLNonNull(updateBlogEntryInputType)).build())
            .dataFetcher(environment -> blogEntryDataFetcher.updateBlogEntry(getInputMap(environment)))
            .build();

        deleteBlogEntryField = newFieldDefinition()
            .name("deleteBlogEntry").description("Delete existing blogEntry")
            .type(blogEntryType)
            .argument(newArgument().name(INPUT).type(new GraphQLNonNull(deleteBlogEntryInputType)).build())
            .dataFetcher(environment -> blogEntryDataFetcher.deleteBlogEntry(getInputMap(environment)))
            .build();
    }
}
