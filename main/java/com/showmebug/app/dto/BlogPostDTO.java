package com.showmebug.app.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BlogPostDTO {
    private Long id;
    private String title;
    private String content;
}
