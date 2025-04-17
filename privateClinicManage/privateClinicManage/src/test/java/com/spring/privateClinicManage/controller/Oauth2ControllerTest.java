package com.spring.privateClinicManage.controller;

import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.oauth2.Dto.CustomOAuth2User;
import com.spring.privateClinicManage.service.JwtService;
import com.spring.privateClinicManage.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit test cho Oauth2Controller - kiểm tra các trường hợp tạo token khi đăng nhập OAuth2
 */
class Oauth2ControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private JwtService jwtService;
    @Mock
    private OAuth2AuthenticationToken authentication;
    @Mock
    private HttpServletResponse response;
    @Mock
    private CustomOAuth2User customOAuth2User;

    @InjectMocks
    private Oauth2Controller oauth2Controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        oauth2Controller = new Oauth2Controller();
        // Inject các dependency private bằng ReflectionTestUtils
        ReflectionTestUtils.setField(oauth2Controller, "userService", userService);
        ReflectionTestUtils.setField(oauth2Controller, "jwtService", jwtService);
    }

    /**
     * TC01: Kiểm tra tạo token sau khi đăng nhập OAuth2 thành công
     */
    @Test
    @DisplayName("TC01: Tạo token thành công khi đăng nhập OAuth2")
    void getOauth2Token_Success() throws IOException {
        // Mock dữ liệu user hợp lệ
        String email = "test@example.com";
        User user = mock(User.class);
        when(user.getEmail()).thenReturn(email);
        when(user.getActive()).thenReturn(true);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", email);

        // Mock các hành vi
        when(authentication.getPrincipal()).thenReturn(customOAuth2User);
        when(customOAuth2User.getAttributes()).thenReturn(attributes);
        when(userService.findByEmail(email)).thenReturn(user);
        when(jwtService.generateTokenLogin(email)).thenReturn("testToken");

        // Gọi hàm và kiểm tra kết quả
        ResponseEntity<Object> result = oauth2Controller.getOauth2Token(authentication, response);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).isEqualTo("testToken");
        verify(response).sendRedirect("http://localhost:3000?token=testToken");
    }

    /**
     * TC02: Kiểm tra xử lý khi authentication null
     */
    @Test
    @DisplayName("TC02: Xử lý khi authentication null")
    void getOauth2Token_AuthenticationNull() throws IOException {
        // Gọi hàm với authentication null
        ResponseEntity<Object> result = oauth2Controller.getOauth2Token(null, response);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(result.getBody()).isEqualTo("UNAUTHORIZED");
        // Không gọi sendRedirect
        verify(response, never()).sendRedirect(any());
    }

    /**
     * TC03: Kiểm tra xử lý khi user không tồn tại
     */
    @Test
    @DisplayName("TC03: Xử lý khi user không tồn tại")
    void getOauth2Token_UserNotFound() throws IOException {
        // Mock dữ liệu user không tồn tại
        String email = "notfound@example.com";
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", email);

        when(authentication.getPrincipal()).thenReturn(customOAuth2User);
        when(customOAuth2User.getAttributes()).thenReturn(attributes);
        when(userService.findByEmail(email)).thenReturn(null);

        // Gọi hàm và kiểm tra kết quả
        ResponseEntity<Object> result = oauth2Controller.getOauth2Token(authentication, response);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(result.getBody()).isEqualTo("Người dùng không tồn tại");
        verify(response, never()).sendRedirect(any());
    }

    /**
     * TC04: Kiểm tra xử lý khi user bị khóa
     */
    @Test
    @DisplayName("TC04: Xử lý khi user bị khóa")
    void getOauth2Token_UserLocked() throws IOException {
        // Mock dữ liệu user bị khóa
        String email = "locked@example.com";
        User user = mock(User.class);
        when(user.getEmail()).thenReturn(email);
        when(user.getActive()).thenReturn(false);
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", email);

        when(authentication.getPrincipal()).thenReturn(customOAuth2User);
        when(customOAuth2User.getAttributes()).thenReturn(attributes);
        when(userService.findByEmail(email)).thenReturn(user);

        // Gọi hàm và kiểm tra kết quả
        ResponseEntity<Object> result = oauth2Controller.getOauth2Token(authentication, response);
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(result.getBody()).isEqualTo("Tài khoản đã bị khóa");
        verify(response, never()).sendRedirect(any());
    }
} 