package com.spring.privateClinicManage.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.UnsupportedEncodingException;

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
import com.spring.privateClinicManage.service.PaymentVNPAYDetailService;
import com.spring.privateClinicManage.service.UserService;

@ExtendWith(MockitoExtension.class)
public class ApiVNPAYPaymentControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PaymentVNPAYDetailService paymentVNPAYDetailService;

    @Mock
    private MedicalRegistryListService medicalRegistryListService;

    @InjectMocks
    private ApiVNPAYPaymentController apiVNPAYPaymentController;

    private PaymentInitDto paymentInitDto;
    private User testUser;
    private MedicalRegistryList testMedicalRegistry;
    private StatusIsApproved statusIsApproved;

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
    }

    /**
     * Test Case: TC_VNPAY_001
     * Description: Test successful VNPAY payment initialization
     * Input: Valid PaymentInitDto with existing user and medical registry
     * Expected Output: Success response with payment URL
     */
    @Test
    void testPaymentPhase1_Success() {
        try {
            // Arrange
            when(userService.getCurrentLoginUser()).thenReturn(testUser);
            when(medicalRegistryListService.findById(1)).thenReturn(testMedicalRegistry);
            when(paymentVNPAYDetailService.generateUrlPayment(anyLong(), any(), any()))
                    .thenReturn("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html");

            // Act
            ResponseEntity<String> response = apiVNPAYPaymentController.paymentPhase1(paymentInitDto);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertTrue(response.getBody().contains("vnpayment.vn"));
        } catch (UnsupportedEncodingException e) {
            fail("Unexpected UnsupportedEncodingException: " + e.getMessage());
        }
    }

    /**
     * Test Case: TC_VNPAY_002
     * Description: Test VNPAY payment with non-existent user
     * Input: PaymentInitDto with non-existent user
     * Expected Output: NOT_FOUND response with error message
     */
    @Test
    void testPaymentPhase1_UserNotFound() {
        try {
            // Arrange
            when(userService.getCurrentLoginUser()).thenReturn(null);

            // Act
            ResponseEntity<String> response = apiVNPAYPaymentController.paymentPhase1(paymentInitDto);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Người dùng không tồn tại", response.getBody());
        } catch (UnsupportedEncodingException e) {
            fail("Unexpected UnsupportedEncodingException: " + e.getMessage());
        }
    }

    /**
     * Test Case: TC_VNPAY_003
     * Description: Test VNPAY payment with non-existent medical registry
     * Input: PaymentInitDto with non-existent medical registry
     * Expected Output: NOT_FOUND response with error message
     */
    @Test
    void testPaymentPhase1_MedicalRegistryNotFound() {
        try {
            // Arrange
            when(userService.getCurrentLoginUser()).thenReturn(testUser);
            when(medicalRegistryListService.findById(1)).thenReturn(null);

            // Act
            ResponseEntity<String> response = apiVNPAYPaymentController.paymentPhase1(paymentInitDto);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Phiếu khám không tồn tại", response.getBody());
        } catch (UnsupportedEncodingException e) {
            fail("Unexpected UnsupportedEncodingException: " + e.getMessage());
        }
    }

    /**
     * Test Case: TC_VNPAY_004
     * Description: Test VNPAY payment with unauthorized user
     * Input: PaymentInitDto with user not owning the medical registry
     * Expected Output: NOT_FOUND response with error message
     */
    @Test
    void testPaymentPhase1_UnauthorizedUser() {
        try {
            // Arrange
            User differentUser = new User();
            differentUser.setId(2);
            differentUser.setEmail("different@example.com");
            differentUser.setName("Different User");

            when(userService.getCurrentLoginUser()).thenReturn(differentUser);
            when(medicalRegistryListService.findById(1)).thenReturn(testMedicalRegistry);

            // Act
            ResponseEntity<String> response = apiVNPAYPaymentController.paymentPhase1(paymentInitDto);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Người dùng này không có phiếu khám này !", response.getBody());
        } catch (UnsupportedEncodingException e) {
            fail("Unexpected UnsupportedEncodingException: " + e.getMessage());
        }
    }
} 