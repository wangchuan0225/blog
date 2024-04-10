package com.showmebug.app.controller;

import com.showmebug.app.dto.CommentDTO;
import com.showmebug.app.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public CommentDTO addComment(@RequestParam("blogId") Long blogId, @RequestBody CommentDTO comment) {
        return commentService.saveCommentForBlog(blogId, comment);
    }

    @PostMapping("/{parentCommentId}/reply")
    public CommentDTO replyComment(@PathVariable Long parentCommentId, @RequestBody CommentDTO reply) {
        return commentService.saveReply(parentCommentId, reply);
    }

}
