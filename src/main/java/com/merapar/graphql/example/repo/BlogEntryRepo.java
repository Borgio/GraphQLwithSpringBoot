package com.merapar.graphql.example.repo;

import com.merapar.graphql.example.model.BlogEntry;
import lombok.Getter;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class BlogEntryRepo {

    @Getter
    private Map<Long, BlogEntry> blogEntries = new HashMap<>();

    @PostConstruct
    public void postConstruct() {
        val blogEntry1 = new BlogEntry(1L, "blog entry 1", 1L);
        val blogEntry2 = new BlogEntry(2L, "blog entry 2", 2L);
        val blogEntry3 = new BlogEntry(3L, "blog entry 3", 1L);
        val blogEntry4 = new BlogEntry(4L, "blog entry 4", 2L);

        blogEntries.put(1L, blogEntry1);
        blogEntries.put(2L, blogEntry2);
        blogEntries.put(3L, blogEntry3);
        blogEntries.put(4L, blogEntry4);
    }
}
