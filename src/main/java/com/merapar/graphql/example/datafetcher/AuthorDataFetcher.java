package com.merapar.graphql.example.datafetcher;

import com.merapar.graphql.base.TypedValueMap;
import com.merapar.graphql.example.model.Author;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AuthorDataFetcher {

    private Map<Long, Author> authors = new HashMap<Long, Author>();

    public List<Author> getAuthorsByFilter(TypedValueMap arguments) {
        Integer id = arguments.get("id");

        if (id != null) {
            return Collections.singletonList(authors.get(id));
        } else {
            return new ArrayList<>(authors.values());
        }
    }

    public Author addAuthor(TypedValueMap arguments) {
        val author = new Author();

        author.setId(arguments.get("id"));
        author.setName(arguments.get("name"));
        author.setBio(arguments.get("bio"));
        author.setEmail(arguments.get("email"));

        authors.put(author.getId(), author);

        return author;
    }

    public Author updateAuthor(TypedValueMap arguments) {
        val author = authors.get(arguments.get("id"));

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
        val author = authors.get(id);

        authors.remove(id);

        return author;
    }

}
