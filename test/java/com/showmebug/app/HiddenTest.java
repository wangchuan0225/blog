package com.showmebug.app;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.showmebug.app.dto.BlogPostCommentDTO;
import com.showmebug.app.dto.BlogPostDTO;
import com.showmebug.app.dto.CommentDTO;
import com.showmebug.app.dto.NestedCommentDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HiddenTest {

    private static final String BLOG_URL = "/api/blogs";
    private static final String COMMENT_URL = "/api/comment";

    private HttpHeaders headers;

    @Autowired
    TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @DisplayName("隐藏用例1")
    void testSolution1() throws Exception {
        // 初始博客列表
        List<?> initialBlogList = restTemplate.getForObject(BLOG_URL, List.class);
        String requestBody = """
                {
                    "title": "Example",
                    "content": "This is a example content."
                }
                """;
        HttpEntity<String> httpEntity = new HttpEntity<>(requestBody, headers);
        // 新增一个博客
        restTemplate.postForObject(BLOG_URL, httpEntity, BlogPostDTO.class);
        // 新增博客后的博客列表
        List<?> blogList = restTemplate.getForObject(BLOG_URL, List.class);
        // 断言博客的数量 + 1
        assertEquals(1, blogList.size() - initialBlogList.size());
    }

    @Test
    @DisplayName("隐藏用例2")
    void testSolution2() throws Exception {
        // 新增一个博客
        String createBlogBody = """
                {
                    "title": "Example",
                    "content": "This is a example content."
                }
                """;
        HttpEntity<String> createBlogHttpEntity = new HttpEntity<>(createBlogBody, headers);
        BlogPostDTO blog = restTemplate.postForObject(BLOG_URL, createBlogHttpEntity, BlogPostDTO.class);

        String createCommentBody = """
                {
                    "author": "Tom",
                    "content": "Hi"
                }
                """;
        HttpEntity<String> createCommentHttpEntity = new HttpEntity<>(createCommentBody, headers);
        String url = COMMENT_URL + "?blogId=" + blog.getId();
        // 为 title 为 Example 的博客添加一条评论
        restTemplate.postForObject(url, createCommentHttpEntity, CommentDTO.class);

        String blogUrl = BLOG_URL + "/" + blog.getId();
        // 获取 title 为 Example 的博客详细信息
        BlogPostCommentDTO blogPostCommentDTO = restTemplate.getForObject(blogUrl, BlogPostCommentDTO.class);

        assertAll(
                () -> assertEquals("Example", blogPostCommentDTO.getTitle()),
                () -> assertEquals(1, blogPostCommentDTO.getComments().size()),
                () -> assertEquals("Tom", blogPostCommentDTO.getComments().get(0).getAuthor()),
                () -> assertEquals("Hi", blogPostCommentDTO.getComments().get(0).getContent()));
    }

    @Test
    @DisplayName("隐藏用例3")
    void testSolution3() throws Exception {
        // 新增一个博客
        String createBlogBody = """
                {
                    "title": "Example",
                    "content": "This is a example content."
                }
                """;
        HttpEntity<String> createBlogHttpEntity = new HttpEntity<>(createBlogBody, headers);
        BlogPostDTO blog = restTemplate.postForObject(BLOG_URL, createBlogHttpEntity, BlogPostDTO.class);

        String createCommentBody = """
                {
                    "author": "Tom",
                    "content": "Hi"
                }
                """;
        HttpEntity<String> createCommentHttpEntity = new HttpEntity<>(createCommentBody, headers);
        String url = COMMENT_URL + "?blogId=" + blog.getId();
        // 为 title 为 Example 的博客添加一条评论
        CommentDTO comment = restTemplate.postForObject(url, createCommentHttpEntity, CommentDTO.class);

        String replyCommentBody = """
                {
                    "author": "Jerry",
                    "content": "Hello"
                }
                """;
        HttpEntity<String> replyCommentHttpEntity = new HttpEntity<>(replyCommentBody, headers);
        String replyUrl = String.format("%s/%d/reply", COMMENT_URL, comment.getId());
        // 回复评论
        restTemplate.postForObject(replyUrl, replyCommentHttpEntity, CommentDTO.class);

        String blogUrl = BLOG_URL + "/" + blog.getId();
        // 获取 title 为 Example 的博客详细信息
        BlogPostCommentDTO blogPostCommentDTO = restTemplate.getForObject(blogUrl, BlogPostCommentDTO.class);

        assertEquals("Example", blogPostCommentDTO.getTitle());
        assertEquals(1, blogPostCommentDTO.getComments().size());

        List<NestedCommentDTO> childComments = blogPostCommentDTO.getComments().get(0).getChildComments();
        assertAll(
                () -> assertEquals("Example", blogPostCommentDTO.getTitle()),
                () -> assertEquals(1, childComments.size()),
                () -> assertEquals("Jerry", childComments.get(0).getAuthor()),
                () -> assertEquals("Hello", childComments.get(0).getContent()));
    }

}