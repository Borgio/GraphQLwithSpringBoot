package com.merapar.graphql.example.datafetcher;

import com.merapar.graphql.base.TypedValueMap;
import com.merapar.graphql.example.model.Comment;
import com.merapar.graphql.example.repo.CommentRepo;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CommentDataFetcher {

    @Autowired
    private CommentRepo commentRepo;

    public List<Comment> getCommentsByFilter(TypedValueMap arguments) {
        Long id = arguments.get("id");

        if (id != null) {
            return Collections.singletonList(commentRepo.getComments().get(id));
        } else {
            return new ArrayList<>(commentRepo.getComments().values());
        }
    }

    public Comment addComment(TypedValueMap arguments) {
        val comment = new Comment();

        comment.setId(arguments.get("id"));
        comment.setAuthor(arguments.get("author"));
        comment.setComment(arguments.get("comment"));
        comment.setBlogEntryId(arguments.get("blogEntryId"));

        commentRepo.getComments().put(comment.getId(), comment);

        return comment;
    }

    public Comment updateComment(TypedValueMap arguments) {
        val comment = commentRepo.getComments().get(arguments.get("id"));

        if (arguments.containsKey("author")) {
            comment.setAuthor(arguments.get("author"));
        }
        if (arguments.containsKey("comment")) {
            comment.setComment(arguments.get("comment"));
        }
        if (arguments.containsKey("blogEntryId")) {
            comment.setComment(arguments.get("blogEntryId"));
        }

        return comment;
    }

    public Comment deleteComment(TypedValueMap arguments) {
        Long id = arguments.get("id");
        val comment = commentRepo.getComments().get(id);

        commentRepo.getComments().remove(id);

        return comment;
    }
}
