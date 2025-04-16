package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spring.privateClinicManage.entity.Blog;
import com.spring.privateClinicManage.entity.LikeBlog;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.LikeBlogRepository;
import com.spring.privateClinicManage.service.impl.LikeBlogServiceImpl;

/**
 * Unit tests for LikeBlogServiceImpl
 * This class tests the like blog service implementation for the Q&A functionality
 */
@ExtendWith(MockitoExtension.class)
public class LikeBlogServiceTest {

    @Mock
    private LikeBlogRepository likeBlogRepository;

    @InjectMocks
    private LikeBlogServiceImpl likeBlogService;

    // Test data
    private LikeBlog testLikeBlog;
    private User testUser;
    private Blog testBlog;

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

        // Create test like blog
        testLikeBlog = new LikeBlog();
        testLikeBlog.setId(1);
        testLikeBlog.setUser(testUser);
        testLikeBlog.setBlog(testBlog);
        testLikeBlog.setHasLiked(true);
    }

    /**
     * Test case: TC_LIKE_BLOG_01
     * Test saving a valid LikeBlog object
     * Input: Valid LikeBlog object
     * Expected output: Repository save method is called with the correct object
     */
    @Test
    @DisplayName("TC_LIKE_BLOG_01: Save valid LikeBlog")
    void testSaveLikeBlog_ValidLikeBlog() {
        // Arrange
        when(likeBlogRepository.save(any(LikeBlog.class))).thenReturn(testLikeBlog);

        // Act
        likeBlogService.saveLikeBlog(testLikeBlog);

        // Assert
        verify(likeBlogRepository, times(1)).save(testLikeBlog);
    }

    /**
     * Test case: TC_LIKE_BLOG_02
     * Test finding a like blog by user and blog when it exists
     * Input: Valid User and Blog objects
     * Expected output: The corresponding LikeBlog object
     */
    @Test
    @DisplayName("TC_LIKE_BLOG_02: Find like blog by user and blog when it exists")
    void testFindLikeBlogByUserAndBlog_ExistingLike() {
        // Arrange
        when(likeBlogRepository.findLikeBlogByUserAndBlog(testUser, testBlog)).thenReturn(testLikeBlog);

        // Act
        LikeBlog result = likeBlogService.findLikeBlogByUserAndBlog(testUser, testBlog);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertTrue(result.getHasLiked());
        assertEquals(testUser, result.getUser());
        assertEquals(testBlog, result.getBlog());
        verify(likeBlogRepository, times(1)).findLikeBlogByUserAndBlog(testUser, testBlog);
    }

    /**
     * Test case: TC_LIKE_BLOG_03
     * Test finding a like blog by user and blog when it doesn't exist
     * Input: Valid User and Blog objects with no existing like
     * Expected output: null
     */
    @Test
    @DisplayName("TC_LIKE_BLOG_03: Find like blog by user and blog when it doesn't exist")
    void testFindLikeBlogByUserAndBlog_NonExistingLike() {
        // Arrange
        User anotherUser = new User();
        anotherUser.setId(2);
        
        when(likeBlogRepository.findLikeBlogByUserAndBlog(anotherUser, testBlog)).thenReturn(null);

        // Act
        LikeBlog result = likeBlogService.findLikeBlogByUserAndBlog(anotherUser, testBlog);

        // Assert
        assertNull(result);
        verify(likeBlogRepository, times(1)).findLikeBlogByUserAndBlog(anotherUser, testBlog);
    }

    /**
     * Test case: TC_LIKE_BLOG_04
     * Test deleting a like blog
     * Input: Valid LikeBlog object
     * Expected output: Repository delete method is called with the correct object
     */
    @Test
    @DisplayName("TC_LIKE_BLOG_04: Delete like blog")
    void testDeleteLikeBlog() {
        // Arrange
        doNothing().when(likeBlogRepository).delete(any(LikeBlog.class));

        // Act
        likeBlogService.deleteLikeBlog(testLikeBlog);

        // Assert
        verify(likeBlogRepository, times(1)).delete(testLikeBlog);
    }

    /**
     * Test case: TC_LIKE_BLOG_05
     * Test counting likes for a blog
     * Input: Valid Blog object
     * Expected output: Count of likes for the blog
     */
    @Test
    @DisplayName("TC_LIKE_BLOG_05: Count likes for a blog")
    void testCountLikeBlogByBlog() {
        // Arrange
        when(likeBlogRepository.countLikeBlogByBlog(testBlog)).thenReturn(5);

        // Act
        Integer count = likeBlogService.countLikeBlogByBlog(testBlog);

        // Assert
        assertEquals(5, count);
        verify(likeBlogRepository, times(1)).countLikeBlogByBlog(testBlog);
    }
}
