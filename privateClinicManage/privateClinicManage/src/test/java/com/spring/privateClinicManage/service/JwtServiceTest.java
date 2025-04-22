package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.jwt.secretkey=testSecretKey123456789012345678901234567890"
})
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private String validToken;
    private String invalidToken;
    private String tokenWithoutBearer;

    @BeforeEach
    void setUp() {
        // Setup valid token
        validToken = jwtService.generateTokenLogin("test@example.com");
        if (!validToken.startsWith("Bearer ")) {
            validToken = "Bearer " + validToken;
        }

        // Setup invalid token
        invalidToken = "Bearer invalid.token.here";

        // Setup token without Bearer
        tokenWithoutBearer = validToken.substring(7);
    }

    @Test
    @DisplayName("TCJWT1: Tạo JWT token với email hợp lệ")
    void generateTokenLogin_ValidEmail_ReturnsToken() {
        String token = jwtService.generateTokenLogin("test@example.com");
        assertNotNull(token);
        assertTrue(token.length() > 0);
    }

    @Test
    @DisplayName("TCJWT2: Xử lý khi email null")
    void generateTokenLogin_NullEmail_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            jwtService.generateTokenLogin(null);
        });
    }

    @Test
    @DisplayName("TCJWT3: Xử lý khi email rỗng")
    void generateTokenLogin_EmptyEmail_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            jwtService.generateTokenLogin("");
        });
    }

    @Test
    @DisplayName("TCJWT4: Lấy claims từ token hợp lệ")
    void getClaimsFromToken_ValidToken_ReturnsClaims() throws ParseException {
        JWTClaimsSet claims = jwtService.getClaimsFromToken(validToken);
        assertNotNull(claims);
        assertEquals("test@example.com", claims.getStringClaim("email"));
    }

    @Test
    @DisplayName("TCJWT5: Xử lý token không hợp lệ")
    void getClaimsFromToken_InvalidToken_ThrowsException() {
        assertThrows(ParseException.class, () -> {
            jwtService.getClaimsFromToken(invalidToken);
        });
    }

    @Test
    @DisplayName("TCJWT6: Xử lý token rỗng")
    void getClaimsFromToken_EmptyToken_ThrowsException() {
        assertThrows(ParseException.class, () -> {
            jwtService.getClaimsFromToken("");
        });
    }

    @Test
    @DisplayName("TCJWT7: Xử lý token không có Bearer")
    void getClaimsFromToken_TokenWithoutBearer_ReturnsClaims() throws ParseException {
        JWTClaimsSet claims = jwtService.getClaimsFromToken(tokenWithoutBearer);
        assertNotNull(claims);
        assertEquals("test@example.com", claims.getStringClaim("email"));
    }

    @Test
    @DisplayName("TCJWT8: Lấy thời gian hết hạn từ token")
    void getExpirationDateFromToken_ValidToken_ReturnsDate() throws ParseException {
        Date expiration = jwtService.getExpirationDateFromToken(validToken);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    @DisplayName("TCJWT9: Xử lý token không có expiration")
    void getExpirationDateFromToken_InvalidToken_ThrowsException() {
        assertThrows(ParseException.class, () -> {
            jwtService.getExpirationDateFromToken(invalidToken);
        });
    }

    @Test
    @DisplayName("TCJWT10: Xử lý token rỗng khi lấy expiration")
    void getExpirationDateFromToken_EmptyToken_ThrowsException() {
        assertThrows(ParseException.class, () -> {
            jwtService.getExpirationDateFromToken("");
        });
    }

    @Test
    @DisplayName("TCJWT11: Lấy email từ token")
    void getEmailFromToken_ValidToken_ReturnsEmail() {
        String email = jwtService.getEmailFromToken(validToken);
        assertEquals("test@example.com", email);
    }

    @Test
    @DisplayName("TCJWT12: Xử lý token không có email")
    void getEmailFromToken_InvalidToken_ReturnsNull() {
        String email = jwtService.getEmailFromToken(invalidToken);
        assertNull(email);
    }

    @Test
    @DisplayName("TCJWT13: Xử lý token rỗng khi lấy email")
    void getEmailFromToken_EmptyToken_ReturnsNull() {
        String email = jwtService.getEmailFromToken("");
        assertNull(email);
    }

    @Test
    @DisplayName("TCJWT14: Kiểm tra token còn hiệu lực")
    void isTokenExpired_ValidToken_ReturnsFalse() throws ParseException {
        boolean isExpired = jwtService.isTokenExpired(validToken);
        assertFalse(isExpired);
    }

    @Test
    @DisplayName("TCJWT15: Xử lý token không hợp lệ khi kiểm tra hết hạn")
    void isTokenExpired_InvalidToken_ThrowsException() {
        assertThrows(ParseException.class, () -> {
            jwtService.isTokenExpired(invalidToken);
        });
    }

    @Test
    @DisplayName("TCJWT16: Xử lý token rỗng khi kiểm tra hết hạn")
    void isTokenExpired_EmptyToken_ThrowsException() {
        assertThrows(ParseException.class, () -> {
            jwtService.isTokenExpired("");
        });
    }

    @Test
    @DisplayName("TCJWT17: Validate token thành công")
    void validateTokenLogin_ValidToken_ReturnsTrue() throws ParseException {
        boolean isValid = jwtService.validateTokenLogin(validToken);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("TCJWT18: Xử lý token không hợp lệ")
    void validateTokenLogin_InvalidToken_ReturnsFalse() throws ParseException {
        boolean isValid = jwtService.validateTokenLogin(invalidToken);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("TCJWT19: Xử lý token rỗng")
    void validateTokenLogin_EmptyToken_ReturnsFalse() throws ParseException {
        boolean isValid = jwtService.validateTokenLogin("");
        assertFalse(isValid);
    }

    @Test
    @DisplayName("TCJWT20: Xử lý token null")
    void validateTokenLogin_NullToken_ReturnsFalse() throws ParseException {
        boolean isValid = jwtService.validateTokenLogin(null);
        assertFalse(isValid);
    }
} 