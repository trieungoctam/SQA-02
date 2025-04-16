package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spring.privateClinicManage.entity.Blog;
import com.spring.privateClinicManage.entity.Comment;
import com.spring.privateClinicManage.entity.CommentBlog;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.CommentBlogRepository;
import com.spring.privateClinicManage.service.impl.CommentBlogServiceImpl;

/**
 * Unit tests for CommentBlogServiceImpl
 * This class tests the comment blog service implementation for the Q&A functionality
 */
@ExtendWith(MockitoExtension.class)
public class CommentBlogServiceTest {

    @Mock
    private CommentBlogRepository commentBlogRepository;

    @InjectMocks
    private CommentBlogServiceImpl commentBlogService;

    // Test data
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
     * Test case: TC_COMMENT_BLOG_01
     * Test saving a valid CommentBlog object
     * Input: Valid CommentBlog object
     * Expected output: Repository save method is called with the correct object
     */
    @Test
    @DisplayName("TC_COMMENT_BLOG_01: Save valid CommentBlog")
    void testSaveCommentBlog_ValidCommentBlog() {
        // Arrange
        when(commentBlogRepository.save(any(CommentBlog.class))).thenReturn(testCommentBlog);

        // Act
        commentBlogService.saveCommentBlog(testCommentBlog);

        // Assert
        verify(commentBlogRepository, times(1)).save(testCommentBlog);
    }

    /**
     * Test case: TC_COMMENT_BLOG_02
     * Test finding comments for a blog when comments exist
     * Input: Valid Blog object with existing comments
     * Expected output: List of comments for the blog
     */
    @Test
    @DisplayName("TC_COMMENT_BLOG_02: Find comments for a blog when comments exist")
    void testFindByBlog_ExistingComments() {
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
     * Test case: TC_COMMENT_BLOG_03
     * Test finding comments for a blog when no comments exist
     * Input: Valid Blog object with no comments
     * Expected output: Empty list
     */
    @Test
    @DisplayName("TC_COMMENT_BLOG_03: Find comments for a blog when no comments exist")
    void testFindByBlog_NoComments() {
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
