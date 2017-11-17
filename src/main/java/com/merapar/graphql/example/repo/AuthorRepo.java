package com.merapar.graphql.example.repo;

import com.merapar.graphql.example.model.Author;
import lombok.Getter;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthorRepo {

    @Getter
    private Map<Long, Author> authors = new HashMap<>();

    @PostConstruct
    public void postConstruct() {
        val author1 = new Author(1L, "author 1", "author1@example.com", "Python dev");
        val author2 = new Author(2L, "author 2", "author2@example.com", "Java dev");
        authors.put(1L, author1);
        authors.put(2L, author2);
    }
}
