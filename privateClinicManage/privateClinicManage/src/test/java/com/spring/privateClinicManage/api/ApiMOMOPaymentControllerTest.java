package com.spring.privateClinicManage.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.spring.privateClinicManage.dto.PaymentInitDto;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.service.MedicalRegistryListService;
import com.spring.privateClinicManage.service.PaymentMOMODetailService;
import com.spring.privateClinicManage.service.UserService;

@ExtendWith(MockitoExtension.class)
public class ApiMOMOPaymentControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PaymentMOMODetailService paymentMOMODetailService;

    @Mock
    private MedicalRegistryListService medicalRegistryListService;

    @InjectMocks
    private ApiMOMOPaymentController apiMOMOPaymentController;

    private PaymentInitDto paymentInitDto;
    private User testUser;
    private MedicalRegistryList testMedicalRegistry;
    private StatusIsApproved statusIsApproved;
    private Map<String, Object> mockMomoResponse;

    @BeforeEach
    void setUp() {
        paymentInitDto = new PaymentInitDto();
        paymentInitDto.setAmount(100000L);
        paymentInitDto.setMrlId(1);
        paymentInitDto.setVoucherId(null);
        paymentInitDto.setMeId(null);

        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@example.com");
        testUser.setName("Test User");

        statusIsApproved = new StatusIsApproved();
        statusIsApproved.setId(1);
        statusIsApproved.setStatus("PAYMENTPHASE1");

        testMedicalRegistry = new MedicalRegistryList();
        testMedicalRegistry.setId(1);
        testMedicalRegistry.setUser(testUser);
        testMedicalRegistry.setStatusIsApproved(statusIsApproved);
        testMedicalRegistry.setIsCanceled(false);

        mockMomoResponse = new HashMap<>();
        mockMomoResponse.put("resultCode", "0");
        mockMomoResponse.put("payUrl", "https://test-payment.momo.vn/gw_payment/transactionProcessor");
    }

    /**
     * Test Case: TC_MOMO_001
     * Description: Test successful MOMO payment initialization
     * Input: Valid PaymentInitDto with existing user and medical registry
     * Expected Output: Success response with payment URL
     */
    @Test
    void testPayment_Success() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(testUser);
        when(medicalRegistryListService.findById(1)).thenReturn(testMedicalRegistry);
        when(paymentMOMODetailService.generateMOMOUrlPayment(anyLong(), any(), any()))
                .thenReturn(mockMomoResponse);

        // Act
        ResponseEntity<Object> response = apiMOMOPaymentController.payment(paymentInitDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().toString().contains("momo.vn"));
    }

    /**
     * Test Case: TC_MOMO_002
     * Description: Test MOMO payment with non-existent user
     * Input: PaymentInitDto with non-existent user
     * Expected Output: NOT_FOUND response with error message
     */
    @Test
    void testPayment_UserNotFound() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiMOMOPaymentController.payment(paymentInitDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng không tồn tại", response.getBody());
    }

    /**
     * Test Case: TC_MOMO_003
     * Description: Test MOMO payment with non-existent medical registry
     * Input: PaymentInitDto with non-existent medical registry
     * Expected Output: NOT_FOUND response with error message
     */
    @Test
    void testPayment_MedicalRegistryNotFound() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(testUser);
        when(medicalRegistryListService.findById(1)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiMOMOPaymentController.payment(paymentInitDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Phiếu khám không tồn tại", response.getBody());
    }

    /**
     * Test Case: TC_MOMO_004
     * Description: Test MOMO payment with failed payment initialization
     * Input: Valid PaymentInitDto but MOMO service returns error
     * Expected Output: BAD_REQUEST response with error message
     */
    @Test
    void testPayment_PaymentInitFailed() {
        // Arrange
        Map<String, Object> failedResponse = new HashMap<>();
        failedResponse.put("resultCode", "1");
        failedResponse.put("message", "Payment failed");

        when(userService.getCurrentLoginUser()).thenReturn(testUser);
        when(medicalRegistryListService.findById(1)).thenReturn(testMedicalRegistry);
        when(paymentMOMODetailService.generateMOMOUrlPayment(anyLong(), any(), any()))
                .thenReturn(failedResponse);

        // Act
        ResponseEntity<Object> response = apiMOMOPaymentController.payment(paymentInitDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Thanh toán thất bại"));
    }
} 