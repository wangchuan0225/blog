package com.showmebug.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.showmebug.app.model.BlogPost;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {

    /**
     * 通过博客 ID 查询对应博客及其评论
     * 
     * @param id 博客 ID
     * @return 博客信息
     */
    @Query("SELECT bp FROM BlogPost bp LEFT JOIN FETCH bp.comments WHERE bp.id = :id")
    Optional<BlogPost> findByIdWithComments(@Param("id") Long id);
}
