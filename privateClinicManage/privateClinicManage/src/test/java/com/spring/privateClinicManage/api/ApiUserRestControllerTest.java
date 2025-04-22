package com.spring.privateClinicManage.api;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.privateClinicManage.dto.UserRegisterDto;
import com.spring.privateClinicManage.entity.Role;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.UserRepository;
import com.spring.privateClinicManage.service.JwtService;
import com.spring.privateClinicManage.service.MailSenderService;
import com.spring.privateClinicManage.service.MedicalRegistryListService;
import com.spring.privateClinicManage.service.RoleService;
import com.spring.privateClinicManage.service.UserService;
import com.spring.privateClinicManage.service.VerifyEmailService;

@WebMvcTest(ApiUserRestController.class)
class ApiUserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private VerifyEmailService verifyEmailService;

    @MockBean
    private MailSenderService mailSenderService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RoleService roleService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MedicalRegistryListService medicalRegistryListService;

    private ObjectMapper objectMapper;
    private UserRegisterDto mockRegisterDto;
    private User mockUser;
    private Role mockRole;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        
        // Setup mock role
        mockRole = new Role();
        mockRole.setId(1);
        mockRole.setName("ROLE_BENHNHAN");

        // Setup mock user
        mockUser = new User();
        mockUser.setId(1);
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("encodedPassword");
        mockUser.setName("Test User");
        mockUser.setPhone("0912345678");
        mockUser.setActive(true);
        mockUser.setRole(mockRole);
        
        // Setup mock register DTO
        mockRegisterDto = new UserRegisterDto();
        mockRegisterDto.setEmail("test@example.com");
        mockRegisterDto.setPassword("Password123!");
        mockRegisterDto.setName("Test User");
        mockRegisterDto.setPhone("0912345678");
    }

    @Test
    @DisplayName("TCAPI1: Đăng ký thành công")
    void register_Success() throws Exception {
        when(userService.isValidGmail(mockRegisterDto.getEmail())).thenReturn(true);
        when(userService.isValidPhoneNumber(mockRegisterDto.getPhone())).thenReturn(true);
        when(userRepository.findByEmail(mockRegisterDto.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(mockRegisterDto.getPassword())).thenReturn("encodedPassword");
        when(roleService.findByName("ROLE_BENHNHAN")).thenReturn(mockRole);
        
        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRegisterDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"));
    }

    @Test
    @DisplayName("TCAPI2: Đăng ký với email không hợp lệ")
    void register_InvalidEmail() throws Exception {
        when(userService.isValidGmail(mockRegisterDto.getEmail())).thenReturn(false);
        
        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRegisterDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    @DisplayName("TC3: Đăng ký với email đã tồn tại")
    void register_ExistingEmail() throws Exception {
        when(userService.isValidGmail(mockRegisterDto.getEmail())).thenReturn(true);
        when(userRepository.findByEmail(mockRegisterDto.getEmail())).thenReturn(mockUser);
        
        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRegisterDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    @DisplayName("TC4: Đăng ký với số điện thoại không hợp lệ")
    void register_InvalidPhone() throws Exception {
        when(userService.isValidGmail(mockRegisterDto.getEmail())).thenReturn(true);
        when(userService.isValidPhoneNumber(mockRegisterDto.getPhone())).thenReturn(false);
        
        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRegisterDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    @DisplayName("TC5: Đăng ký thiếu thông tin bắt buộc")
    void register_MissingRequiredFields() throws Exception {
        mockRegisterDto.setEmail(null);
        mockRegisterDto.setPassword(null);
        
        mockMvc.perform(post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockRegisterDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    @DisplayName("TC6: Đăng nhập thành công")
    void login_Success() throws Exception {
        when(userService.authUser("test@example.com", "Password123!")).thenReturn(true);
        when(jwtService.generateTokenLogin("test@example.com")).thenReturn("mockToken");
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        when(passwordEncoder.matches("Password123!", mockUser.getPassword())).thenReturn(true);
        
        mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"Password123!\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("TC7: Đăng nhập thất bại")
    void login_Failure() throws Exception {
        when(userService.authUser("test@example.com", "wrongpass")).thenReturn(false);
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        when(passwordEncoder.matches("wrongpass", mockUser.getPassword())).thenReturn(false);
        
        mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"wrongpass\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    @DisplayName("TC8: Đăng nhập với email không tồn tại")
    void login_NonExistentEmail() throws Exception {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);
        
        mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"nonexistent@example.com\",\"password\":\"anypass\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("error"));
    }

    @Test
    @DisplayName("TC9: Đăng nhập với tài khoản bị khóa")
    void login_InactiveAccount() throws Exception {
        mockUser.setActive(false);
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        
        mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@example.com\",\"password\":\"Password123!\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("error"));
    }
} 