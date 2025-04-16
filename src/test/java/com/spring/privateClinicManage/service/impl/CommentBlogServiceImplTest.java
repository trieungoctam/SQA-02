package com.spring.privateClinicManage.service.impl;

import com.spring.privateClinicManage.entity.Blog;
import com.spring.privateClinicManage.entity.Comment;
import com.spring.privateClinicManage.entity.CommentBlog;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.CommentBlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CommentBlogServiceImpl class
 * Tests the functionality related to commenting on blogs/questions
 */
@ExtendWith(MockitoExtension.class)
public class CommentBlogServiceImplTest {

    @Mock
    private CommentBlogRepository commentBlogRepository;

    @InjectMocks
    private CommentBlogServiceImpl commentBlogService;

    private CommentBlog testCommentBlog;
    private User testUser;
    private Blog testBlog;
    private Comment testComment;
    private List<CommentBlog> commentBlogList;

    /**
     * Setup test data before each test
     */
    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setId(1);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setAvatar("avatar.jpg");

        // Create test blog
        testBlog = new Blog();
        testBlog.setId(1);
        testBlog.setTitle("Test Question");
        testBlog.setContent("This is a test question content");
        testBlog.setCreatedDate(new Date());
        testBlog.setUser(testUser);

        // Create test comment
        testComment = new Comment();
        testComment.setId(1);
        testComment.setContent("This is a test comment");
        testComment.setCreatedDate(new Date());
        testComment.setUser(testUser);

        // Create test comment blog
        testCommentBlog = new CommentBlog();
        testCommentBlog.setId(1);
        testCommentBlog.setBlog(testBlog);
        testCommentBlog.setComment(testComment);

        // Create comment blog list
        commentBlogList = new ArrayList<>();
        commentBlogList.add(testCommentBlog);
    }

    /**
     * Test case ID: COMMENT-BLOG-SERVICE-01
     * Test objective: Verify that a comment blog can be saved successfully
     * Input: A valid CommentBlog object
     * Expected output: CommentBlog is saved in the repository
     */
    @Test
    @DisplayName("Test saveCommentBlog method")
    void testSaveCommentBlog() {
        // Arrange
        doNothing().when(commentBlogRepository).save(any(CommentBlog.class));

        // Act
        commentBlogService.saveCommentBlog(testCommentBlog);

        // Assert
        verify(commentBlogRepository, times(1)).save(testCommentBlog);
    }

    /**
     * Test case ID: COMMENT-BLOG-SERVICE-02
     * Test objective: Verify that findByBlog returns comments for a specific blog
     * Input: Valid Blog object
     * Expected output: List of comments for the blog
     */
    @Test
    @DisplayName("Test findByBlog method with existing comments")
    void testFindByBlogWithExistingComments() {
        // Arrange
        when(commentBlogRepository.findByBlog(testBlog)).thenReturn(commentBlogList);

        // Act
        List<CommentBlog> result = commentBlogService.findByBlog(testBlog);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testBlog, result.get(0).getBlog());
        assertEquals(testComment, result.get(0).getComment());
        verify(commentBlogRepository, times(1)).findByBlog(testBlog);
    }

    /**
     * Test case ID: COMMENT-BLOG-SERVICE-03
     * Test objective: Verify that findByBlog returns empty list when no comments exist
     * Input: Valid Blog object with no comments
     * Expected output: Empty list
     */
    @Test
    @DisplayName("Test findByBlog method with no comments")
    void testFindByBlogWithNoComments() {
        // Arrange
        Blog anotherBlog = new Blog();
        anotherBlog.setId(2);
        anotherBlog.setTitle("Another Blog");
        
        when(commentBlogRepository.findByBlog(anotherBlog)).thenReturn(new ArrayList<>());

        // Act
        List<CommentBlog> result = commentBlogService.findByBlog(anotherBlog);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(commentBlogRepository, times(1)).findByBlog(anotherBlog);
    }
}
