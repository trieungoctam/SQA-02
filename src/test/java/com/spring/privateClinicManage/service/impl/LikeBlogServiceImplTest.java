package com.spring.privateClinicManage.service.impl;

import com.spring.privateClinicManage.entity.Blog;
import com.spring.privateClinicManage.entity.LikeBlog;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.LikeBlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for LikeBlogServiceImpl class
 * Tests the functionality related to liking blogs/questions
 */
@ExtendWith(MockitoExtension.class)
public class LikeBlogServiceImplTest {

    @Mock
    private LikeBlogRepository likeBlogRepository;

    @InjectMocks
    private LikeBlogServiceImpl likeBlogService;

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
     * Test case ID: LIKE-BLOG-SERVICE-01
     * Test objective: Verify that a like blog can be saved successfully
     * Input: A valid LikeBlog object
     * Expected output: LikeBlog is saved in the repository
     */
    @Test
    @DisplayName("Test saveLikeBlog method")
    void testSaveLikeBlog() {
        // Arrange
        doNothing().when(likeBlogRepository).save(any(LikeBlog.class));

        // Act
        likeBlogService.saveLikeBlog(testLikeBlog);

        // Assert
        verify(likeBlogRepository, times(1)).save(testLikeBlog);
    }

    /**
     * Test case ID: LIKE-BLOG-SERVICE-02
     * Test objective: Verify that findLikeBlogByUserAndBlog returns a like when it exists
     * Input: Valid User and Blog objects
     * Expected output: The corresponding LikeBlog object
     */
    @Test
    @DisplayName("Test findLikeBlogByUserAndBlog method with existing like")
    void testFindLikeBlogByUserAndBlogWithExistingLike() {
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
     * Test case ID: LIKE-BLOG-SERVICE-03
     * Test objective: Verify that findLikeBlogByUserAndBlog returns null when like doesn't exist
     * Input: Valid User and Blog objects with no existing like
     * Expected output: null
     */
    @Test
    @DisplayName("Test findLikeBlogByUserAndBlog method with non-existing like")
    void testFindLikeBlogByUserAndBlogWithNonExistingLike() {
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
     * Test case ID: LIKE-BLOG-SERVICE-04
     * Test objective: Verify that deleteLikeBlog deletes a like blog
     * Input: Valid LikeBlog object
     * Expected output: LikeBlog is deleted from the repository
     */
    @Test
    @DisplayName("Test deleteLikeBlog method")
    void testDeleteLikeBlog() {
        // Arrange
        doNothing().when(likeBlogRepository).delete(any(LikeBlog.class));

        // Act
        likeBlogService.deleteLikeBlog(testLikeBlog);

        // Assert
        verify(likeBlogRepository, times(1)).delete(testLikeBlog);
    }

    /**
     * Test case ID: LIKE-BLOG-SERVICE-05
     * Test objective: Verify that countLikeBlogByBlog returns correct count
     * Input: Valid Blog object
     * Expected output: Count of likes for the blog
     */
    @Test
    @DisplayName("Test countLikeBlogByBlog method")
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
