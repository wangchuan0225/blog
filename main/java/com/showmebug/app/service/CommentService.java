package com.showmebug.app.service;

import org.springframework.stereotype.Service;

import com.showmebug.app.dto.CommentDTO;
import com.showmebug.app.model.BlogPost;
import com.showmebug.app.model.Comment;
import com.showmebug.app.repository.BlogPostRepository;
import com.showmebug.app.repository.CommentRepository;

@Service
public class CommentService {

    private final BlogPostRepository blogPostRepository;
    private final CommentRepository commentRepository;

    public CommentService(BlogPostRepository blogPostRepository, CommentRepository commentRepository) {
        this.blogPostRepository = blogPostRepository;
        this.commentRepository = commentRepository;
    }

    public CommentDTO saveCommentForBlog(Long blogId, CommentDTO commentDTO) {
        BlogPost blogPost = blogPostRepository.findById(blogId)
                .orElseThrow(() -> new IllegalArgumentException("Blog not found"));
        Comment comment = new Comment();
        comment.setAuthor(commentDTO.getAuthor());
        comment.setContent(commentDTO.getContent());
        comment.setBlogPost(blogPost);
        Comment save = commentRepository.save(comment);
        commentDTO.setId(save.getId());
        return commentDTO;
    }

    public CommentDTO saveReply(Long parentCommentId, CommentDTO reply) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new IllegalArgumentException("Parent comment not found"));
        Comment comment = new Comment();
        comment.setAuthor(reply.getAuthor());
        comment.setContent(reply.getContent());
        comment.setParentComment(parentComment);

        Comment save = commentRepository.save(comment);
        reply.setId(save.getId());
        return reply;
    }
}
