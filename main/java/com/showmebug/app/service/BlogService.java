package com.showmebug.app.service;

import java.util.*;
import java.util.stream.Collectors;

import com.showmebug.app.repository.CommentRepository;
import org.springframework.stereotype.Service;

import com.showmebug.app.dto.BlogPostCommentDTO;
import com.showmebug.app.dto.BlogPostDTO;
import com.showmebug.app.dto.NestedCommentDTO;
import com.showmebug.app.model.BlogPost;
import com.showmebug.app.model.Comment;
import com.showmebug.app.repository.BlogPostRepository;

@Service
public class BlogService {

    private final BlogPostRepository blogPostRepository;

    private final CommentRepository commentRepository;

    public BlogService(BlogPostRepository blogPostRepository, CommentRepository commentRepository) {
        this.blogPostRepository = blogPostRepository;
        this.commentRepository = commentRepository;
    }

    public List<BlogPostDTO> findAll() {
        // return blogPostRepository.findAll().stream().map(this::findByBlogPost).toList();
        return blogPostRepository.findAll().stream().map(this::convertToBlogPostDTO).toList();
    }

    public BlogPostCommentDTO findById(Long id) {
        // 验证博客是否存在
        BlogPost blogPost = blogPostRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Blog not found"));

        return findByBlogPost(blogPost);
    }

    public BlogPostDTO save(BlogPostDTO blogPostDTO) {
        BlogPost blog = new BlogPost();
        blog.setTitle(blogPostDTO.getTitle());
        blog.setContent(blogPostDTO.getContent());
        return convertToBlogPostDTO(blogPostRepository.save(blog));
    }

    private BlogPostCommentDTO findByBlogPost(BlogPost blogPost) {
        // 加载顶层评论
        Set<Comment> topLevelComments = commentRepository.findTopLevelComments(blogPost.getId());

        // 加载顶层评论的子评论
        Map<Long, List<Comment>> childCommentsMap = loadChildrenComments(topLevelComments);
        return convertToBlogPostDTO(blogPost, topLevelComments, childCommentsMap);
    }

    private Map<Long, List<Comment>> loadChildrenComments(Set<Comment> topLevelComments) {
        Map<Long, List<Comment>> childCommentsMap = new HashMap<>();
        Set<Long> parentCommentIds = topLevelComments.stream().map(Comment::getId).collect(Collectors.toSet());

        while (!parentCommentIds.isEmpty()) {
            List<Comment> childComments = commentRepository.findByParentCommentIds(parentCommentIds);
            parentCommentIds.clear();
            for (Comment child : childComments) {
                childCommentsMap.computeIfAbsent(child.getParentComment().getId(), k -> new ArrayList<>()).add(child);
                parentCommentIds.add(child.getId());
            }
        }
        return childCommentsMap;
    }

    private BlogPostCommentDTO convertToBlogPostDTO(BlogPost blogPost, Set<Comment> topLevelComments, Map<Long, List<Comment>> childCommentsMap) {
        BlogPostCommentDTO dto = new BlogPostCommentDTO();
        dto.setId(blogPost.getId());
        dto.setTitle(blogPost.getTitle());
        dto.setContent(blogPost.getContent());
        dto.setComments(topLevelComments.stream()
                .map(comment -> convertToCommentSummaryDTO(comment, childCommentsMap))
                .collect(Collectors.toList()));
        return dto;
    }

    private NestedCommentDTO convertToCommentSummaryDTO(Comment comment, Map<Long, List<Comment>> childCommentsMap) {
        NestedCommentDTO dto = new NestedCommentDTO();
        dto.setId(comment.getId());
        dto.setAuthor(comment.getAuthor());
        dto.setContent(comment.getContent());
        List<NestedCommentDTO> childDtos = childCommentsMap.getOrDefault(comment.getId(), Collections.emptyList())
                .stream()
                .map(childComment -> convertToCommentSummaryDTO(childComment, childCommentsMap))
                .collect(Collectors.toList());
        dto.setChildComments(childDtos);
        return dto;
    }

    private BlogPostDTO convertToBlogPostDTO(BlogPost blogPost) {
        BlogPostDTO dto = new BlogPostDTO();
        dto.setId(blogPost.getId());
        dto.setTitle(blogPost.getTitle());
        dto.setContent(blogPost.getContent());
        return dto;
    }
}
