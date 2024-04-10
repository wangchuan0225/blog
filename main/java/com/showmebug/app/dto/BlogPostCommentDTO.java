package com.showmebug.app.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BlogPostCommentDTO extends BlogPostDTO{
    private List<NestedCommentDTO> comments;
}
