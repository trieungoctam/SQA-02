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

import com.spring.privateClinicManage.entity.PaymentDetailPhase2;
import com.spring.privateClinicManage.repository.PaymentDetailPhase2Repository;
import com.spring.privateClinicManage.service.impl.PaymentDetailPhase2ServiceImpl;

/**
 * Unit tests for PaymentDetailPhase2ServiceImpl
 * This class tests the Phase 2 payment service implementation
 */
@ExtendWith(MockitoExtension.class)
public class PaymentDetailPhase2ServiceImplTest {

    @Mock
    private PaymentDetailPhase2Repository paymentDetailPhase2Repository;

    @InjectMocks
    private PaymentDetailPhase2ServiceImpl paymentDetailPhase2Service;

    // Test data
    private PaymentDetailPhase2 paymentDetailPhase2;

    /**
     * Setup test data before each test
     */
    @BeforeEach
    void setUp() {
        // Initialize test data
        paymentDetailPhase2 = new PaymentDetailPhase2();
        paymentDetailPhase2.setOrderId("TEST789012");
        paymentDetailPhase2.setAmount(200000L);
        paymentDetailPhase2.setDescription("Test payment phase 2");
        paymentDetailPhase2.setResultCode("0");
        paymentDetailPhase2.setPartnerCode("MOMO");
        paymentDetailPhase2.setCreatedDate(new Date());
    }

    /**
     * Test case: TC_PDP2_01
     * Test saving a valid PaymentDetailPhase2 object
     * Input: Valid PaymentDetailPhase2 object
     * Expected output: Repository save method is called with the correct object
     */
    @Test
    @DisplayName("TC_PDP2_01: Save valid PaymentDetailPhase2")
    void testSavePdp2_ValidPayment() {
        // Arrange
        when(paymentDetailPhase2Repository.save(any(PaymentDetailPhase2.class)))
                .thenReturn(paymentDetailPhase2);

        // Act
        paymentDetailPhase2Service.savePdp2(paymentDetailPhase2);

        // Assert
        verify(paymentDetailPhase2Repository, times(1)).save(paymentDetailPhase2);
    }

    /**
     * Test case: TC_PDP2_02
     * Test saving a null PaymentDetailPhase2 object
     * Input: Null PaymentDetailPhase2 object
     * Expected output: NullPointerException is thrown when repository tries to save null
     */
    @Test
    @DisplayName("TC_PDP2_02: Save null PaymentDetailPhase2")
    void testSavePdp2_NullPayment() {
        // Arrange
        // Mock repository to throw NullPointerException when null is passed
        when(paymentDetailPhase2Repository.save(null)).thenThrow(NullPointerException.class);

        // Assert
        assertThrows(NullPointerException.class, () -> {
            paymentDetailPhase2Service.savePdp2(null);
        });

        // Verify repository was called with null
        verify(paymentDetailPhase2Repository).save(null);
    }

    /**
     * Test case: TC_PDP2_03
     * Test saving a PaymentDetailPhase2 object with null orderId
     * Input: PaymentDetailPhase2 object with null orderId
     * Expected output: Repository save method is called, but may throw exception in real DB
     * Note: This test verifies the service behavior, not the DB constraints
     */
    @Test
    @DisplayName("TC_PDP2_03: Save PaymentDetailPhase2 with null orderId")
    void testSavePdp2_NullOrderId() {
        // Arrange
        paymentDetailPhase2.setOrderId(null);

        // Act & Assert
        // In a real DB this would fail due to not-null constraint, but we're testing service behavior
        paymentDetailPhase2Service.savePdp2(paymentDetailPhase2);

        // Verify repository was called
        verify(paymentDetailPhase2Repository, times(1)).save(paymentDetailPhase2);
    }

    /**
     * Test case: TC_PDP2_04
     * Test saving a PaymentDetailPhase2 object with null amount
     * Input: PaymentDetailPhase2 object with null amount
     * Expected output: Repository save method is called, but may throw exception in real DB
     * Note: This test verifies the service behavior, not the DB constraints
     */
    @Test
    @DisplayName("TC_PDP2_04: Save PaymentDetailPhase2 with null amount")
    void testSavePdp2_NullAmount() {
        // Arrange
        paymentDetailPhase2.setAmount(null);

        // Act & Assert
        // In a real DB this would fail due to not-null constraint, but we're testing service behavior
        paymentDetailPhase2Service.savePdp2(paymentDetailPhase2);

        // Verify repository was called
        verify(paymentDetailPhase2Repository, times(1)).save(paymentDetailPhase2);
    }
    
    /**
     * Test case: TC_PDP2_05
     * Test saving a PaymentDetailPhase2 object with zero amount
     * Input: PaymentDetailPhase2 object with zero amount
     * Expected output: Repository save method is called (current implementation behavior)
     * Note: This test verifies the current behavior. In a real implementation,
     * validation for zero amounts should be added.
     */
    @Test
    @DisplayName("TC_PDP2_05: Save PaymentDetailPhase2 with zero amount")
    void testSavePdp2_ZeroAmount() {
        // Arrange
        paymentDetailPhase2.setAmount(0L);

        // Act & Assert
        // The service should be updated to validate this case
        paymentDetailPhase2Service.savePdp2(paymentDetailPhase2);

        // Verify repository was called
        verify(paymentDetailPhase2Repository, times(1)).save(paymentDetailPhase2);
    }
    
    /**
     * Test case: TC_PDP2_06
     * Test saving a PaymentDetailPhase2 object with negative amount
     * Input: PaymentDetailPhase2 object with negative amount
     * Expected output: Repository save method is called (current implementation behavior)
     * Note: This test verifies the current behavior. In a real implementation,
     * validation for negative amounts should be added.
     */
    @Test
    @DisplayName("TC_PDP2_06: Save PaymentDetailPhase2 with negative amount")
    void testSavePdp2_NegativeAmount() {
        // Arrange
        paymentDetailPhase2.setAmount(-10000L);

        // Act & Assert
        // The service should be updated to validate this case
        paymentDetailPhase2Service.savePdp2(paymentDetailPhase2);

        // Verify repository was called
        verify(paymentDetailPhase2Repository, times(1)).save(paymentDetailPhase2);
    }
}
