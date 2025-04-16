package com.spring.privateClinicManage.service.impl;

import com.spring.privateClinicManage.entity.Blog;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.BlogRepository;
import com.spring.privateClinicManage.service.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BlogServiceImpl class
 * Tests the functionality related to blog/question management
 */
@ExtendWith(MockitoExtension.class)
public class BlogServiceImplTest {

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private BlogServiceImpl blogService;

    private Blog testBlog;
    private User testUser;
    private List<Blog> blogList;

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

        // Create blog list
        blogList = new ArrayList<>();
        blogList.add(testBlog);

        // Add a second blog
        Blog secondBlog = new Blog();
        secondBlog.setId(2);
        secondBlog.setTitle("Second Question");
        secondBlog.setContent("This is another test question");
        secondBlog.setCreatedDate(new Date());
        secondBlog.setUser(testUser);
        blogList.add(secondBlog);
    }

    /**
     * Test case ID: BLOG-SERVICE-01
     * Test objective: Verify that a blog can be saved successfully
     * Input: A valid Blog object
     * Expected output: Blog is saved in the repository
     */
    @Test
    @DisplayName("Test saveBlog method")
    void testSaveBlog() {
        // Arrange
        doNothing().when(blogRepository).save(any(Blog.class));

        // Act
        blogService.saveBlog(testBlog);

        // Assert
        verify(blogRepository, times(1)).save(testBlog);
    }

    /**
     * Test case ID: BLOG-SERVICE-02
     * Test objective: Verify that findById returns a blog when it exists
     * Input: Valid blog ID
     * Expected output: The corresponding Blog object
     */
    @Test
    @DisplayName("Test findById method with existing blog")
    void testFindByIdWithExistingBlog() {
        // Arrange
        when(blogRepository.findById(1)).thenReturn(Optional.of(testBlog));

        // Act
        Blog result = blogService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Question", result.getTitle());
        verify(blogRepository, times(1)).findById(1);
    }

    /**
     * Test case ID: BLOG-SERVICE-03
     * Test objective: Verify that findById returns null when blog doesn't exist
     * Input: Non-existent blog ID
     * Expected output: null
     */
    @Test
    @DisplayName("Test findById method with non-existing blog")
    void testFindByIdWithNonExistingBlog() {
        // Arrange
        when(blogRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Blog result = blogService.findById(999);

        // Assert
        assertNull(result);
        verify(blogRepository, times(1)).findById(999);
    }

    /**
     * Test case ID: BLOG-SERVICE-04
     * Test objective: Verify that findAllBlogsByUser returns blogs for a specific user
     * Input: Valid User object
     * Expected output: List of blogs belonging to the user
     */
    @Test
    @DisplayName("Test findAllBlogsByUser method")
    void testFindAllBlogsByUser() {
        // Arrange
        when(blogRepository.findAllBlogsByUser(testUser)).thenReturn(blogList);

        // Act
        List<Blog> result = blogService.findAllBlogsByUser(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Question", result.get(0).getTitle());
        assertEquals("Second Question", result.get(1).getTitle());
        verify(blogRepository, times(1)).findAllBlogsByUser(testUser);
    }

    /**
     * Test case ID: BLOG-SERVICE-05
     * Test objective: Verify that countBlogByCurrentUser returns correct count
     * Input: Valid User object
     * Expected output: Count of blogs for the user
     */
    @Test
    @DisplayName("Test countBlogByCurrentUser method")
    void testCountBlogByCurrentUser() {
        // Arrange
        when(blogRepository.countBlogByCurrentUser(testUser)).thenReturn(2);

        // Act
        Integer count = blogService.countBlogByCurrentUser(testUser);

        // Assert
        assertEquals(2, count);
        verify(blogRepository, times(1)).countBlogByCurrentUser(testUser);
    }

    /**
     * Test case ID: BLOG-SERVICE-06
     * Test objective: Verify that findAllBlogs returns all blogs
     * Input: None
     * Expected output: List of all blogs
     */
    @Test
    @DisplayName("Test findAllBlogs method")
    void testFindAllBlogs() {
        // Arrange
        when(blogRepository.findAll()).thenReturn(blogList);

        // Act
        List<Blog> result = blogService.findAllBlogs();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(blogRepository, times(1)).findAll();
    }

    /**
     * Test case ID: BLOG-SERVICE-07
     * Test objective: Verify that findByAnyKey returns blogs matching the search key
     * Input: Search key
     * Expected output: List of blogs matching the search criteria
     */
    @Test
    @DisplayName("Test findByAnyKey method")
    void testFindByAnyKey() {
        // Arrange
        String searchKey = "Test";
        when(blogRepository.findBlogsByAnyKey(searchKey)).thenReturn(blogList);

        // Act
        List<Blog> result = blogService.findByAnyKey(searchKey);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(blogRepository, times(1)).findBlogsByAnyKey(searchKey);
    }

    /**
     * Test case ID: BLOG-SERVICE-08
     * Test objective: Verify that allBlogsPaginated returns paginated blogs
     * Input: Page number, size, and list of blogs
     * Expected output: Paginated blogs
     */
    @Test
    @DisplayName("Test allBlogsPaginated method")
    void testAllBlogsPaginated() {
        // Arrange
        int page = 1;
        int size = 10;
        
        // Act
        Page<Blog> result = blogService.allBlogsPaginated(page, size, blogList);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getNumber()); // Page is 0-indexed in PageRequest
    }
}
