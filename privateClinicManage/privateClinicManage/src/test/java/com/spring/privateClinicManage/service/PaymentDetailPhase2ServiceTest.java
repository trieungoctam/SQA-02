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

import com.spring.privateClinicManage.entity.PaymentDetailPhase2;
import com.spring.privateClinicManage.repository.PaymentDetailPhase2Repository;
import com.spring.privateClinicManage.service.impl.PaymentDetailPhase2ServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PaymentDetailPhase2ServiceTest {

    @Mock
    private PaymentDetailPhase2Repository paymentDetailPhase2Repository;

    @InjectMocks
    private PaymentDetailPhase2ServiceImpl paymentDetailPhase2Service;

    private PaymentDetailPhase2 testPayment;

    @BeforeEach
    void setUp() {
        testPayment = new PaymentDetailPhase2();
        testPayment.setId(1);
        testPayment.setOrderId("ORDER456");
        testPayment.setAmount(200000L);
        testPayment.setDescription("Test payment phase 2");
        testPayment.setResultCode("00");
        testPayment.setPartnerCode("MOMO");
    }

    /**
     * Test Case: TC_PDP2_001
     * Description: Test successful saving of payment detail phase 2
     * Input: Valid PaymentDetailPhase2 object
     * Expected Output: Payment is saved successfully
     */
    @Test
    @Transactional
    void testSavePaymentDetailPhase2_Success() {
        // Arrange
        when(paymentDetailPhase2Repository.save(any(PaymentDetailPhase2.class))).thenReturn(testPayment);

        // Act
        paymentDetailPhase2Service.savePdp2(testPayment);

        // Assert
        verify(paymentDetailPhase2Repository, times(1)).save(testPayment);
    }

    /**
     * Test Case: TC_PDP2_002
     * Description: Test handling null payment detail phase 2
     * Input: Null PaymentDetailPhase2 object
     * Expected Output: No exception is thrown (current implementation behavior)
     * Note: This test verifies the current behavior. In a real implementation,
     * validation for null input should be added.
     */
    @Test
    @Transactional
    void testSavePaymentDetailPhase2_NullInput() {
        // Act & Assert - Just verifying current behavior
        // The service should be updated to validate this case
        assertDoesNotThrow(() -> {
            paymentDetailPhase2Service.savePdp2(null);
        });
        
        // Verify that save is called with null (current behavior)
        verify(paymentDetailPhase2Repository, times(1)).save(null);
    }

    /**
     * Test Case: TC_PDP2_003
     * Description: Test handling payment detail phase 2 with zero amount
     * Input: PaymentDetailPhase2 with zero amount
     * Expected Output: No exception is thrown (current implementation behavior)
     * Note: This test verifies the current behavior. In a real implementation,
     * validation for zero amounts should be added.
     */
    @Test
    @Transactional
    void testSavePaymentDetailPhase2_InvalidAmount() {
        // Arrange
        testPayment.setAmount(0L);
        when(paymentDetailPhase2Repository.save(any(PaymentDetailPhase2.class))).thenReturn(testPayment);

        // Act & Assert - Just verifying current behavior
        // The service should be updated to validate this case
        assertDoesNotThrow(() -> {
            paymentDetailPhase2Service.savePdp2(testPayment);
        });
        
        // Verify that save is called with the zero amount (current behavior)
        verify(paymentDetailPhase2Repository, times(1)).save(testPayment);
    }
} 