package com.merapar.graphql.example.datafetcher;

import com.merapar.graphql.base.TypedValueMap;
import com.merapar.graphql.example.model.Author;
import com.merapar.graphql.example.model.BlogEntry;
import com.merapar.graphql.example.model.Comment;
import com.merapar.graphql.example.repo.AuthorRepo;
import com.merapar.graphql.example.repo.BlogEntryRepo;
import com.merapar.graphql.example.repo.CommentRepo;
import graphql.schema.DataFetchingEnvironment;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class BlogEntryDataFetcher {

    @Autowired
    private BlogEntryRepo blogEntryRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @Autowired
    private CommentRepo commentRepo;

    public List<BlogEntry> getBlogEntriesByFilter(TypedValueMap arguments) {
        Long id = arguments.get("id");

        if (id != null) {
            return Collections.singletonList(blogEntryRepo.getBlogEntries().get(id));
        } else {
            return new ArrayList<>(blogEntryRepo.getBlogEntries().values());
        }
    }

    public BlogEntry addBlogEntry(TypedValueMap arguments) {
        val comment = new BlogEntry();

        comment.setId(arguments.get("id"));
        comment.setTitle(arguments.get("title"));
        comment.setAuthorId(arguments.get("authorId"));

        blogEntryRepo.getBlogEntries().put(comment.getId(), comment);

        return comment;
    }

    public BlogEntry updateBlogEntry(TypedValueMap arguments) {
        val blogEntry = blogEntryRepo.getBlogEntries().get(arguments.get("id"));

        if (arguments.containsKey("title")) {
            blogEntry.setTitle(arguments.get("title"));
        }
        if (arguments.containsKey("authorId")) {
            blogEntry.setAuthorId(arguments.get("authorId"));
        }

        return blogEntry;
    }

    public BlogEntry deleteBlogEntry(TypedValueMap arguments) {
        Long id = arguments.get("id");
        val blogEntry = blogEntryRepo.getBlogEntries().get(id);

        blogEntryRepo.getBlogEntries().remove(id);

        return blogEntry;
    }

    public Author getAuthorByAuthorId(DataFetchingEnvironment dataFetchingEnvironment) {
        BlogEntry blogEntry = dataFetchingEnvironment.getSource();
        val author = authorRepo.getAuthors().get(blogEntry.getAuthorId());
        return author;
    }

    public List<Comment> getCommentsByBlogEntryId(DataFetchingEnvironment dataFetchingEnvironment) {
        BlogEntry blogEntry = dataFetchingEnvironment.getSource();
        return commentRepo.getCommentsByBlogEntryId(blogEntry.getId());
    }
}
