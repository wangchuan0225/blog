package com.showmebug.app.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.showmebug.app.dto.BlogPostCommentDTO;
import com.showmebug.app.dto.BlogPostDTO;
import com.showmebug.app.service.BlogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping("/{id}")
    public BlogPostCommentDTO getBlogPostById(@PathVariable long id) {
        return blogService.findById(id);
    }

    @GetMapping
    public List<BlogPostDTO> getAllBlogPosts() {
        return blogService.findAll();
    }

    @PostMapping
    public BlogPostDTO createBlogPost(@RequestBody BlogPostDTO blogPost) {
        return blogService.save(blogPost);
    }
}
