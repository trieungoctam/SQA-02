package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spring.privateClinicManage.config.PaymentVnPayConfig;
import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.entity.Voucher;
import com.spring.privateClinicManage.entity.VoucherCondition;
import com.spring.privateClinicManage.service.impl.PaymentVNPAYDetailServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Unit tests for PaymentVNPAYDetailServiceImpl
 * This class tests the VNPAY payment service implementation
 */
@ExtendWith(MockitoExtension.class)
public class PaymentVNPAYDetailServiceImplTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private PaymentVNPAYDetailServiceImpl paymentVNPAYDetailService;

    // Test data
    private MedicalRegistryList mrl;
    private Voucher voucher;
    private User user;
    private Long amount;

    /**
     * Setup test data before each test
     */
    @BeforeEach
    void setUp() {
        // Initialize test data
        user = new User();
        user.setId(1);
        user.setName("Test User");
        user.setEmail("test@example.com");

        mrl = new MedicalRegistryList();
        mrl.setId(100);
        mrl.setUser(user);

        VoucherCondition voucherCondition = new VoucherCondition();
        voucherCondition.setId(1);
        voucherCondition.setPercentSale(10);

        voucher = new Voucher();
        voucher.setId(1);
        voucher.setCode("TESTCODE");
        voucher.setVoucherCondition(voucherCondition);

        amount = 100000L;

        // Setup PaymentVnPayConfig static fields for testing
        PaymentVnPayConfig.vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        PaymentVnPayConfig.vnp_ReturnUrl = "http://localhost:8888/api/payment/vnpay/return/";
        PaymentVnPayConfig.vnp_TmnCode = "TESTCODE";
        PaymentVnPayConfig.secretKey = "TESTSECRETKEY";
        PaymentVnPayConfig.vnp_ApiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

        // Mock IP address for testing
        when(PaymentVnPayConfig.getIpAddress(request)).thenReturn("127.0.0.1");
    }

    /**
     * Test case: TC_VNPAY_01
     * Test generating VNPAY payment URL with valid inputs
     * Input: Valid amount, MedicalRegistryList, and Voucher
     * Expected output: Valid payment URL containing expected parameters
     */
    @Test
    @DisplayName("TC_VNPAY_01: Generate VNPAY payment URL with valid inputs")
    void testGenerateUrlPayment_ValidInputs() throws UnsupportedEncodingException {
        // Act
        String paymentUrl = paymentVNPAYDetailService.generateUrlPayment(amount, mrl, voucher);

        // Assert
        assertNotNull(paymentUrl);
        assertTrue(paymentUrl.startsWith(PaymentVnPayConfig.vnp_PayUrl));
        assertTrue(paymentUrl.contains("vnp_Amount=" + (amount * 100)));
        assertTrue(paymentUrl.contains("vnp_TmnCode=" + PaymentVnPayConfig.vnp_TmnCode));
        assertTrue(paymentUrl.contains("vnp_ReturnUrl="));
        assertTrue(paymentUrl.contains("vnp_OrderInfo="));
    }

    /**
     * Test case: TC_VNPAY_02
     * Test generating VNPAY payment URL without voucher
     * Input: Valid amount, MedicalRegistryList, null Voucher
     * Expected output: Valid payment URL containing expected parameters
     */
    @Test
    @DisplayName("TC_VNPAY_02: Generate VNPAY payment URL without voucher")
    void testGenerateUrlPayment_WithoutVoucher() throws UnsupportedEncodingException {
        // Act
        String paymentUrl = paymentVNPAYDetailService.generateUrlPayment(amount, mrl, null);

        // Assert
        assertNotNull(paymentUrl);
        assertTrue(paymentUrl.startsWith(PaymentVnPayConfig.vnp_PayUrl));
        assertTrue(paymentUrl.contains("vnp_Amount=" + (amount * 100)));
        assertTrue(paymentUrl.contains("vnp_TmnCode=" + PaymentVnPayConfig.vnp_TmnCode));
    }

    /**
     * Test case: TC_VNPAY_03
     * Test generating VNPAY payment URL with medical examination
     * Input: Valid amount, MedicalRegistryList with MedicalExamination, Voucher
     * Expected output: Valid payment URL containing expected parameters
     */
    @Test
    @DisplayName("TC_VNPAY_03: Generate VNPAY payment URL with medical examination")
    void testGenerateUrlPayment_WithMedicalExamination() throws UnsupportedEncodingException {
        // Arrange
        MedicalExamination me = new MedicalExamination();
        me.setId(200);
        mrl.setMedicalExamination(me);

        // Act
        String paymentUrl = paymentVNPAYDetailService.generateUrlPayment(amount, mrl, voucher);

        // Assert
        assertNotNull(paymentUrl);
        assertTrue(paymentUrl.startsWith(PaymentVnPayConfig.vnp_PayUrl));
        assertTrue(paymentUrl.contains("vnp_Amount=" + (amount * 100)));
        assertTrue(paymentUrl.contains("vnp_TmnCode=" + PaymentVnPayConfig.vnp_TmnCode));
        // Should contain medical examination ID in the order info
        assertTrue(paymentUrl.contains("vnp_OrderInfo="));
    }

    /**
     * Test case: TC_VNPAY_04
     * Test generating VNPAY payment URL with zero amount
     * Input: Zero amount, valid MedicalRegistryList, Voucher
     * Expected output: Valid payment URL with zero amount
     */
    @Test
    @DisplayName("TC_VNPAY_04: Generate VNPAY payment URL with zero amount")
    void testGenerateUrlPayment_ZeroAmount() throws UnsupportedEncodingException {
        // Act
        String paymentUrl = paymentVNPAYDetailService.generateUrlPayment(0L, mrl, voucher);

        // Assert
        assertNotNull(paymentUrl);
        assertTrue(paymentUrl.startsWith(PaymentVnPayConfig.vnp_PayUrl));
        assertTrue(paymentUrl.contains("vnp_Amount=0"));
    }
}
