package com.merapar.graphql.example.datafetcher;

import com.merapar.graphql.base.TypedValueMap;
import com.merapar.graphql.example.model.Author;
import com.merapar.graphql.example.model.BlogEntry;
import com.merapar.graphql.example.repo.AuthorRepo;
import com.merapar.graphql.example.repo.BlogEntryRepo;
import graphql.schema.DataFetchingEnvironment;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class AuthorDataFetcher {

    @Autowired
    private AuthorRepo authorRepo;

    @Autowired
    private BlogEntryRepo blogEntryRepo;

    public List<Author> getAuthorsByFilter(TypedValueMap arguments) {
        Long id = arguments.get("id");

        if (id != null) {
            return Collections.singletonList(authorRepo.getAuthors().get(id));
        } else {
            return new ArrayList<>(authorRepo.getAuthors().values());
        }
    }

    public Author addAuthor(TypedValueMap arguments) {
        val author = new Author();

        author.setId(arguments.get("id"));
        author.setName(arguments.get("name"));
        author.setBio(arguments.get("bio"));
        author.setEmail(arguments.get("email"));

        authorRepo.getAuthors().put(author.getId(), author);

        return author;
    }

    public Author updateAuthor(TypedValueMap arguments) {
        val author = authorRepo.getAuthors().get(arguments.get("id"));

        if (arguments.containsKey("name")) {
            author.setName(arguments.get("name"));
        }
        if (arguments.containsKey("bio")) {
            author.setBio(arguments.get("bio"));
        }
        if (arguments.containsKey("email")) {
            author.setEmail(arguments.get("email"));
        }

        return author;
    }

    public Author deleteAuthor(TypedValueMap arguments) {
        Long id = arguments.get("id");
        val author = authorRepo.getAuthors().get(id);

        authorRepo.getAuthors().remove(id);

        return author;
    }

    public List<BlogEntry> getBlogEntriesByAuthorId(DataFetchingEnvironment dataFetchingEnvironment) {
        Author author = dataFetchingEnvironment.getSource();
        return blogEntryRepo.getBlogEntriesByAuthorId(author.getId());
    }
}
