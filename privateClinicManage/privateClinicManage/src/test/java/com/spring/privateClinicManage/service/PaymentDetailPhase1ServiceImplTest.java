package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spring.privateClinicManage.entity.PaymentDetailPhase1;
import com.spring.privateClinicManage.repository.PaymentDetailPhase1Repository;
import com.spring.privateClinicManage.service.impl.PaymentDetailPhase1ServiceImpl;

/**
 * Unit tests for PaymentDetailPhase1ServiceImpl
 * This class tests the Phase 1 payment service implementation
 */
@ExtendWith(MockitoExtension.class)
public class PaymentDetailPhase1ServiceImplTest {

    @Mock
    private PaymentDetailPhase1Repository paymentDetailPhase1Repository;

    @InjectMocks
    private PaymentDetailPhase1ServiceImpl paymentDetailPhase1Service;

    // Test data
    private PaymentDetailPhase1 paymentDetailPhase1;

    /**
     * Setup test data before each test
     */
    @BeforeEach
    void setUp() {
        // Initialize test data
        paymentDetailPhase1 = new PaymentDetailPhase1();
        paymentDetailPhase1.setOrderId("TEST123456");
        paymentDetailPhase1.setAmount(100000L);
        paymentDetailPhase1.setDescription("Test payment");
        paymentDetailPhase1.setResultCode("0");
        paymentDetailPhase1.setPartnerCode("VNPAY");
        paymentDetailPhase1.setCreatedDate(new Date());
    }

    /**
     * Test case: TC_PDP1_01
     * Test saving a valid PaymentDetailPhase1 object
     * Input: Valid PaymentDetailPhase1 object
     * Expected output: Repository save method is called with the correct object
     */
    @Test
    @DisplayName("TC_PDP1_01: Save valid PaymentDetailPhase1")
    void testSavePdp1_ValidPayment() {
        // Arrange
        when(paymentDetailPhase1Repository.save(any(PaymentDetailPhase1.class)))
                .thenReturn(paymentDetailPhase1);

        // Act
        paymentDetailPhase1Service.savePdp1(paymentDetailPhase1);

        // Assert
        verify(paymentDetailPhase1Repository, times(1)).save(paymentDetailPhase1);
    }

    /**
     * Test case: TC_PDP1_02
     * Test saving a null PaymentDetailPhase1 object
     * Input: Null PaymentDetailPhase1 object
     * Expected output: NullPointerException is thrown when repository tries to save null
     */
    @Test
    @DisplayName("TC_PDP1_02: Save null PaymentDetailPhase1")
    void testSavePdp1_NullPayment() {
        // Arrange
        // Mock repository to throw NullPointerException when null is passed
        when(paymentDetailPhase1Repository.save(null)).thenThrow(NullPointerException.class);

        // Assert
        assertThrows(NullPointerException.class, () -> {
            paymentDetailPhase1Service.savePdp1(null);
        });

        // Verify repository was called with null
        verify(paymentDetailPhase1Repository).save(null);
    }

    /**
     * Test case: TC_PDP1_03
     * Test saving a PaymentDetailPhase1 object with null orderId
     * Input: PaymentDetailPhase1 object with null orderId
     * Expected output: Repository save method is called, but may throw exception in real DB
     * Note: This test verifies the service behavior, not the DB constraints
     */
    @Test
    @DisplayName("TC_PDP1_03: Save PaymentDetailPhase1 with null orderId")
    void testSavePdp1_NullOrderId() {
        // Arrange
        paymentDetailPhase1.setOrderId(null);

        // Act & Assert
        // In a real DB this would fail due to not-null constraint, but we're testing service behavior
        paymentDetailPhase1Service.savePdp1(paymentDetailPhase1);

        // Verify repository was called
        verify(paymentDetailPhase1Repository, times(1)).save(paymentDetailPhase1);
    }

    /**
     * Test case: TC_PDP1_04
     * Test saving a PaymentDetailPhase1 object with null amount
     * Input: PaymentDetailPhase1 object with null amount
     * Expected output: Repository save method is called, but may throw exception in real DB
     * Note: This test verifies the service behavior, not the DB constraints
     */
    @Test
    @DisplayName("TC_PDP1_04: Save PaymentDetailPhase1 with null amount")
    void testSavePdp1_NullAmount() {
        // Arrange
        paymentDetailPhase1.setAmount(null);

        // Act & Assert
        // In a real DB this would fail due to not-null constraint, but we're testing service behavior
        paymentDetailPhase1Service.savePdp1(paymentDetailPhase1);

        // Verify repository was called
        verify(paymentDetailPhase1Repository, times(1)).save(paymentDetailPhase1);
    }
}
