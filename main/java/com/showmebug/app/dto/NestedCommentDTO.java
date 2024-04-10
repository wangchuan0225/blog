package com.showmebug.app.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NestedCommentDTO extends CommentDTO {
    private List<NestedCommentDTO> childComments;
}
