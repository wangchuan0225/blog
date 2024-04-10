package com.showmebug.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.showmebug.app.model.Comment;

import java.util.List;
import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * 根据博客 ID 查询最顶层的评论
     *
     * @param blogPostId 博客 ID
     * @return 评论集合
     */
    @Query("SELECT c FROM Comment c WHERE c.blogPost.id = :blogPostId AND c.parentComment.id IS NULL")
    Set<Comment> findTopLevelComments(Long blogPostId);

    /**
     * 根据父级评论 ID 查询所有符合条件的评论
     *
     * @param parentCommentIds 父级评论 ID
     * @return 评论列表
     */
    @Query("SELECT c FROM Comment c WHERE c.parentComment.id IN :parentCommentIds")
    List<Comment> findByParentCommentIds(Set<Long> parentCommentIds);
}
