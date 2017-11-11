package com.merapar.graphql.example.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String author;

    @Getter
    @Setter
    private String comment;
}
