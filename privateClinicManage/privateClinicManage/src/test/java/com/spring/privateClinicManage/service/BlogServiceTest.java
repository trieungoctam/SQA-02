package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import com.spring.privateClinicManage.entity.Blog;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.BlogRepository;
import com.spring.privateClinicManage.service.impl.BlogServiceImpl;

/**
 * Unit tests for BlogServiceImpl
 * This class tests the blog service implementation for the Q&A functionality
 */
@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {

    @Mock
    private BlogRepository blogRepository;

    @InjectMocks
    private BlogServiceImpl blogService;

    // Test data
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
     * Test case: TC_BLOG_01
     * Test saving a valid Blog object
     * Input: Valid Blog object
     * Expected output: Repository save method is called with the correct object
     */
    @Test
    @DisplayName("TC_BLOG_01: Save valid Blog")
    void testSaveBlog_ValidBlog() {
        // Arrange
        when(blogRepository.save(any(Blog.class))).thenReturn(testBlog);

        // Act
        blogService.saveBlog(testBlog);

        // Assert
        verify(blogRepository, times(1)).save(testBlog);
    }

    /**
     * Test case: TC_BLOG_02
     * Test finding a blog by ID when it exists
     * Input: Valid blog ID
     * Expected output: The corresponding Blog object
     */
    @Test
    @DisplayName("TC_BLOG_02: Find blog by ID when it exists")
    void testFindById_ExistingBlog() {
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
     * Test case: TC_BLOG_03
     * Test finding a blog by ID when it doesn't exist
     * Input: Non-existent blog ID
     * Expected output: null
     */
    @Test
    @DisplayName("TC_BLOG_03: Find blog by ID when it doesn't exist")
    void testFindById_NonExistingBlog() {
        // Arrange
        when(blogRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Blog result = blogService.findById(999);

        // Assert
        assertNull(result);
        verify(blogRepository, times(1)).findById(999);
    }

    /**
     * Test case: TC_BLOG_04
     * Test finding all blogs by user
     * Input: Valid User object
     * Expected output: List of blogs belonging to the user
     */
    @Test
    @DisplayName("TC_BLOG_04: Find all blogs by user")
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
     * Test case: TC_BLOG_05
     * Test counting blogs by current user
     * Input: Valid User object
     * Expected output: Count of blogs for the user
     */
    @Test
    @DisplayName("TC_BLOG_05: Count blogs by current user")
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
     * Test case: TC_BLOG_06
     * Test finding all blogs
     * Input: None
     * Expected output: List of all blogs
     */
    @Test
    @DisplayName("TC_BLOG_06: Find all blogs")
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
     * Test case: TC_BLOG_07
     * Test finding blogs by search key
     * Input: Search key
     * Expected output: List of blogs matching the search criteria
     */
    @Test
    @DisplayName("TC_BLOG_07: Find blogs by search key")
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
     * Test case: TC_BLOG_08
     * Test paginating blogs
     * Input: Page number, size, and list of blogs
     * Expected output: Paginated blogs
     */
    @Test
    @DisplayName("TC_BLOG_08: Paginate blogs")
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
