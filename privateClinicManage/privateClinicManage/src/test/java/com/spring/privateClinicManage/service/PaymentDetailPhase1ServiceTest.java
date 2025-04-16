package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import com.spring.privateClinicManage.entity.PaymentDetailPhase1;
import com.spring.privateClinicManage.repository.PaymentDetailPhase1Repository;
import com.spring.privateClinicManage.service.impl.PaymentDetailPhase1ServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PaymentDetailPhase1ServiceTest {

    @Mock
    private PaymentDetailPhase1Repository paymentDetailPhase1Repository;

    @InjectMocks
    private PaymentDetailPhase1ServiceImpl paymentDetailPhase1Service;

    private PaymentDetailPhase1 testPayment;

    @BeforeEach
    void setUp() {
        testPayment = new PaymentDetailPhase1();
        testPayment.setId(1);
        testPayment.setOrderId("ORDER123");
        testPayment.setAmount(100000L);
        testPayment.setDescription("Test payment");
        testPayment.setResultCode("00");
        testPayment.setPartnerCode("VNPAY");
    }

    /**
     * Test Case: TC_PDP1_001
     * Description: Test successful saving of payment detail phase 1
     * Input: Valid PaymentDetailPhase1 object
     * Expected Output: Payment is saved successfully
     */
    @Test
    @Transactional
    void testSavePaymentDetailPhase1_Success() {
        // Arrange
        when(paymentDetailPhase1Repository.save(any(PaymentDetailPhase1.class))).thenReturn(testPayment);

        // Act
        paymentDetailPhase1Service.savePdp1(testPayment);

        // Assert
        verify(paymentDetailPhase1Repository, times(1)).save(testPayment);
    }

    /**
     * Test Case: TC_PDP1_002
     * Description: Test handling null payment detail phase 1
     * Input: Null PaymentDetailPhase1 object
     * Expected Output: No exception is thrown (current implementation behavior)
     * Note: This test verifies the current behavior. In a real implementation,
     * validation for null input should be added.
     */
    @Test
    @Transactional
    void testSavePaymentDetailPhase1_NullInput() {
        // Act & Assert - Just verifying current behavior
        // The service should be updated to validate this case
        assertDoesNotThrow(() -> {
            paymentDetailPhase1Service.savePdp1(null);
        });

        // Verify that save is called with null (current behavior)
        verify(paymentDetailPhase1Repository, times(1)).save(null);
    }

    /**
     * Test Case: TC_PDP1_003
     * Description: Test handling payment detail phase 1 with negative amount
     * Input: PaymentDetailPhase1 with negative amount
     * Expected Output: No exception is thrown (current implementation behavior)
     * Note: This test verifies the current behavior. In a real implementation,
     * validation for negative amounts should be added.
     */
    @Test
    @Transactional
    void testSavePaymentDetailPhase1_InvalidAmount() {
        // Arrange
        testPayment.setAmount(-100L);
        when(paymentDetailPhase1Repository.save(any(PaymentDetailPhase1.class))).thenReturn(testPayment);

        // Act & Assert - Just verifying current behavior
        // The service should be updated to validate this case
        assertDoesNotThrow(() -> {
            paymentDetailPhase1Service.savePdp1(testPayment);
        });

        // Verify that save is called with the negative amount (current behavior)
        verify(paymentDetailPhase1Repository, times(1)).save(testPayment);
    }
} 