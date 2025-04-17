package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.spring.privateClinicManage.dto.UserRegisterDto;
import com.spring.privateClinicManage.entity.Role;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.UserRepository;
import com.spring.privateClinicManage.service.impl.UserServiceImpl;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RoleService roleService;

    private User mockUser;
    private UserRegisterDto mockRegisterDto;
    private Role mockRole;

    @BeforeEach
    void setUp() {
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
        mockRegisterDto.setEmail("newuser@example.com");
        mockRegisterDto.setPassword("Password123!");
        mockRegisterDto.setName("New User");
        mockRegisterDto.setPhone("0912345678");
        mockRegisterDto.setGender("male");
        mockRegisterDto.setBirthday(new Date());
        mockRegisterDto.setAddress("Test Address");
    }

    @Test
    @DisplayName("TC1: Tìm user với email hợp lệ và tồn tại")
    void findByEmail_ValidEmail_UserExists() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        User result = userService.findByEmail("test@example.com");
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("TC2: Tìm user với email không tồn tại")
    void findByEmail_NonExistentEmail_ReturnsNull() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);
        User result = userService.findByEmail("nonexistent@example.com");
        assertNull(result);
        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    @DisplayName("TC3: Xử lý khi email là null")
    void findByEmail_NullEmail_ReturnsNull() {
        when(userRepository.findByEmail(null)).thenReturn(null);
        User result = userService.findByEmail(null);
        assertNull(result);
        verify(userRepository).findByEmail(null);
    }

    @Test
    @DisplayName("TC4: Lưu thông tin đăng ký hợp lệ")
    void saveUserRegisterDto_ValidData_Success() {
        when(userRepository.findByEmail(mockRegisterDto.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(mockRegisterDto.getPassword())).thenReturn("encodedPassword");
        when(roleService.findByName("ROLE_BENHNHAN")).thenReturn(mockRole);

        userService.saveUserRegisterDto(mockRegisterDto);
        
        verify(userRepository).findByEmail(mockRegisterDto.getEmail());
        verify(passwordEncoder).encode(mockRegisterDto.getPassword());
        verify(roleService).findByName("ROLE_BENHNHAN");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("TC5: Xử lý khi email đã được đăng ký")
    void saveUserRegisterDto_ExistingEmail_ThrowsException() {
        when(userRepository.findByEmail(mockRegisterDto.getEmail())).thenReturn(mockUser);

        assertThrows(RuntimeException.class, () -> {
            userService.saveUserRegisterDto(mockRegisterDto);
        });
        
        verify(userRepository).findByEmail(mockRegisterDto.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("TC6: Validate dữ liệu bắt buộc")
    void saveUserRegisterDto_ValidateRequiredFields() {
        // Test thiếu email
        mockRegisterDto.setEmail(null);
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUserRegisterDto(mockRegisterDto);
        });

        // Test thiếu password
        mockRegisterDto.setEmail("test@example.com");
        mockRegisterDto.setPassword(null);
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUserRegisterDto(mockRegisterDto);
        });

        // Test thiếu tên
        mockRegisterDto.setPassword("Password123!");
        mockRegisterDto.setName(null);
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUserRegisterDto(mockRegisterDto);
        });

        // Test thiếu số điện thoại
        mockRegisterDto.setName("Test User");
        mockRegisterDto.setPhone(null);
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUserRegisterDto(mockRegisterDto);
        });
    }

    @Test
    @DisplayName("TC7: Đăng nhập với thông tin hợp lệ")
    void authUser_ValidCredentials_ReturnsTrue() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        when(passwordEncoder.matches("Password123!", mockUser.getPassword())).thenReturn(true);
        
        assertTrue(userService.authUser("test@example.com", "Password123!"));
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("Password123!", mockUser.getPassword());
    }

    @Test
    @DisplayName("TC8: Xử lý khi nhập sai mật khẩu")
    void authUser_WrongPassword_ReturnsFalse() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        when(passwordEncoder.matches("wrongpassword", mockUser.getPassword())).thenReturn(false);
        
        assertFalse(userService.authUser("test@example.com", "wrongpassword"));
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("wrongpassword", mockUser.getPassword());
    }

    @Test
    @DisplayName("TC9: Xử lý khi email chưa đăng ký")
    void authUser_NonExistentEmail_ReturnsFalse() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);
        
        assertFalse(userService.authUser("nonexistent@example.com", "anypassword"));
        verify(userRepository).findByEmail("nonexistent@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    @DisplayName("TC10: Kiểm tra tài khoản đang hoạt động")
    void isActived_ActiveAccount_ReturnsTrue() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        assertTrue(userService.isActived("test@example.com"));
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("TC11: Kiểm tra tài khoản bị khóa")
    void isActived_InactiveAccount_ReturnsFalse() {
        mockUser.setActive(false);
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        assertFalse(userService.isActived("test@example.com"));
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    @DisplayName("TC12: Tìm user theo SĐT thành công")
    void findByPhone_ValidPhone_UserExists() {
        when(userRepository.findByPhone("0912345678")).thenReturn(mockUser);
        User result = userService.findByPhone("0912345678");
        assertNotNull(result);
        assertEquals("0912345678", result.getPhone());
        verify(userRepository).findByPhone("0912345678");
    }

    @Test
    @DisplayName("TC13: Xử lý khi SĐT không tồn tại")
    void findByPhone_NonExistentPhone_ReturnsNull() {
        when(userRepository.findByPhone("0987654321")).thenReturn(null);
        User result = userService.findByPhone("0987654321");
        assertNull(result);
        verify(userRepository).findByPhone("0987654321");
    }

    @Test
    @DisplayName("TC14: Validate email đúng format")
    void isValidGmail_ValidEmail_ReturnsTrue() {
        assertTrue(userService.isValidGmail("test@gmail.com"));
        assertTrue(userService.isValidGmail("test.user@gmail.com"));
        assertTrue(userService.isValidGmail("test123@gmail.com"));
    }

    @Test
    @DisplayName("TC15: Validate email sai format")
    void isValidGmail_InvalidEmail_ReturnsFalse() {
        // Test với email null hoặc rỗng
        assertFalse(userService.isValidGmail(null));
        assertFalse(userService.isValidGmail(""));
        
        // Test với email không phải gmail
        assertFalse(userService.isValidGmail("test@example.com"));
        assertFalse(userService.isValidGmail("test@yahoo.com"));
        assertFalse(userService.isValidGmail("test@hotmail.com"));
    }

    @Test
    @DisplayName("TC16: Validate số điện thoại đúng format")
    void isValidPhoneNumber_ValidPhone_ReturnsTrue() {
        assertTrue(userService.isValidPhoneNumber("0912345678"));
        assertTrue(userService.isValidPhoneNumber("0987654321"));
        assertTrue(userService.isValidPhoneNumber("0123456789"));
    }

    @Test
    @DisplayName("TC17: Validate số điện thoại có ký tự")
    void isValidPhoneNumber_InvalidPhone_ReturnsFalse() {
        assertFalse(userService.isValidPhoneNumber("123"));
        assertFalse(userService.isValidPhoneNumber("abcdefghij"));
        assertFalse(userService.isValidPhoneNumber("091234567a"));
        assertFalse(userService.isValidPhoneNumber("091234567"));
        assertFalse(userService.isValidPhoneNumber("09123456789"));
        assertFalse(userService.isValidPhoneNumber(null));
        assertFalse(userService.isValidPhoneNumber(""));
    }

    @Test
    @DisplayName("TC18: Kiểm tra đăng ký với email trống")
    void saveUserRegisterDto_EmptyEmail_ThrowsException() {
        mockRegisterDto.setEmail("");
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUserRegisterDto(mockRegisterDto);
        });
    }

    @Test
    @DisplayName("TC19: Kiểm tra đăng ký với mật khẩu trống")
    void saveUserRegisterDto_EmptyPassword_ThrowsException() {
        mockRegisterDto.setPassword("");
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUserRegisterDto(mockRegisterDto);
        });
    }

    @Test
    @DisplayName("TC20: Kiểm tra đăng ký với tên trống")
    void saveUserRegisterDto_EmptyName_ThrowsException() {
        mockRegisterDto.setName("");
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUserRegisterDto(mockRegisterDto);
        });
    }

    @Test
    @DisplayName("TC21: Kiểm tra đăng ký với số điện thoại trống")
    void saveUserRegisterDto_EmptyPhone_ThrowsException() {
        mockRegisterDto.setPhone("");
        assertThrows(IllegalArgumentException.class, () -> {
            userService.saveUserRegisterDto(mockRegisterDto);
        });
    }

    @Test
    @DisplayName("TC22: Kiểm tra đăng nhập với email trống")
    void authUser_EmptyEmail_ReturnsFalse() {
        assertFalse(userService.authUser("", "anypassword"));
    }

    @Test
    @DisplayName("TC23: Kiểm tra đăng nhập với mật khẩu trống")
    void authUser_EmptyPassword_ReturnsFalse() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(mockUser);
        assertFalse(userService.authUser("test@example.com", ""));
    }

    @Test
    @DisplayName("TC24: Kiểm tra tài khoản không tồn tại")
    void isActived_NonExistentUser_ThrowsException() {
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);
        assertThrows(NullPointerException.class, () -> {
            userService.isActived("nonexistent@example.com");
        });
    }

    @Test
    @DisplayName("TC25: Kiểm tra tìm user với số điện thoại trống")
    void findByPhone_EmptyPhone_ReturnsNull() {
        assertNull(userService.findByPhone(""));
    }

    @Test
    @DisplayName("TC26: Kiểm tra tìm user với số điện thoại null")
    void findByPhone_NullPhone_ReturnsNull() {
        assertNull(userService.findByPhone(null));
    }

    @Test
    @DisplayName("TC27: Kiểm tra validate số điện thoại với độ dài không hợp lệ")
    void isValidPhoneNumber_InvalidLength_ReturnsFalse() {
        assertFalse(userService.isValidPhoneNumber("123")); // Quá ngắn
        assertFalse(userService.isValidPhoneNumber("091234567890")); // Quá dài
    }

    @Test
    @DisplayName("TC28: Kiểm tra validate số điện thoại với ký tự đặc biệt")
    void isValidPhoneNumber_SpecialCharacters_ReturnsFalse() {
        assertFalse(userService.isValidPhoneNumber("0912-345-678"));
        assertFalse(userService.isValidPhoneNumber("0912 345 678"));
        assertFalse(userService.isValidPhoneNumber("0912.345.678"));
    }
} 