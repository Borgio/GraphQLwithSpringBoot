package com.merapar.graphql.example.repo;

import com.merapar.graphql.example.model.Comment;
import lombok.Getter;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CommentRepo {

    @Getter
    private Map<Long, Comment> comments = new HashMap<>();

    @PostConstruct
    public void postConstruct() {
        val comment1 = new Comment(1L, "anonymous 1", "comment1", 1L);
        val comment2 = new Comment(1L, "anonymous 2", "comment2", 2L);
        val comment3 = new Comment(1L, "anonymous 3", "comment3", 3L);
        val comment4 = new Comment(1L, "anonymous 4", "comment4", 4L);
        val comment5 = new Comment(1L, "anonymous 5", "comment5", 1L);
        val comment6 = new Comment(1L, "anonymous 6", "comment6", 2L);
        val comment7 = new Comment(1L, "anonymous 7", "comment7", 3L);
        val comment8 = new Comment(1L, "anonymous 8", "comment8", 4L);

        comments.put(1L, comment1);
        comments.put(2L, comment2);
        comments.put(3L, comment3);
        comments.put(4L, comment4);
        comments.put(5L, comment5);
        comments.put(6L, comment6);
        comments.put(7L, comment7);
        comments.put(8L, comment8);
    }

    public List<Comment> getCommentsByBlogEntryId(Long blogEntryId) {
        return comments.values().stream()
            .filter(comment -> comment.getBlogEntryId().equals(blogEntryId))
            .collect(Collectors.toList());
    }
}
