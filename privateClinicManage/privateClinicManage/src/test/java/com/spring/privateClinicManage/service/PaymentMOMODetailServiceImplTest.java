package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.spring.privateClinicManage.config.PaymentMomoConfig;
import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.entity.Voucher;
import com.spring.privateClinicManage.entity.VoucherCondition;
import com.spring.privateClinicManage.service.impl.PaymentMOMODetailServiceImpl;

/**
 * Unit tests for PaymentMOMODetailServiceImpl
 * This class tests the MOMO payment service implementation
 *
 * Các test case kiểm tra chức năng tạo URL thanh toán MOMO với các tình huống khác nhau:
 * - Tạo URL với dữ liệu hợp lệ
 * - Tạo URL không có voucher
 * - Tạo URL với medical examination
 * - Xử lý response lỗi từ MOMO
 */
@ExtendWith(MockitoExtension.class)
public class PaymentMOMODetailServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PaymentMOMODetailServiceImpl paymentMOMODetailService;

    // Test data
    private MedicalRegistryList mrl;
    private Voucher voucher;
    private User user;
    private Long amount;
    private Map<String, Object> momoResponse;

    /**
     * Setup test data before each test
     * Khởi tạo dữ liệu test cho các test case
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

        // Mock MOMO response
        momoResponse = new HashMap<>();
        momoResponse.put("resultCode", "0");
        momoResponse.put("payUrl", "https://test-payment.momo.vn/pay/test-url");

        // Setup PaymentMomoConfig static fields for testing
        PaymentMomoConfig.momo_ApiUrl = "https://test-payment.momo.vn/v2/gateway/api/create";
        PaymentMomoConfig.momo_accessKey = "testAccessKey";
        PaymentMomoConfig.momo_secretKey = "testSecretKey";
        PaymentMomoConfig.momo_partnerCode = "MOMO";
        PaymentMomoConfig.momo_redirectUrl = "http://localhost:8888/api/payment/momo/return/";
    }

    /**
     * Test case: TC_MOMO_01
     * Test generating MOMO payment URL with valid inputs
     * Input: Valid amount, MedicalRegistryList, and Voucher
     * Expected output: Map containing resultCode "0" and valid payUrl
     *
     * Mục tiêu: Kiểm tra việc tạo URL thanh toán MOMO với dữ liệu đầu vào hợp lệ
     * Đầu vào: Số tiền hợp lệ, đối tượng MedicalRegistryList và Voucher
     * Đầu ra mong đợi: Map chứa resultCode "0" và payUrl hợp lệ
     */
    @Test
    @DisplayName("TC_MOMO_01: Generate MOMO payment URL with valid inputs")
    void testGenerateMOMOUrlPayment_ValidInputs() {
        // Arrange
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(momoResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(responseEntity);

        // Act
        Map<String, Object> result = paymentMOMODetailService.generateMOMOUrlPayment(amount, mrl, voucher);

        // Assert
        assertNotNull(result);
        assertEquals("0", result.get("resultCode"));
        assertEquals("https://test-payment.momo.vn/pay/test-url", result.get("payUrl"));

        // Verify that RestTemplate was called with the correct URL
        verify(restTemplate).exchange(eq(PaymentMomoConfig.momo_ApiUrl), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class));
    }

    /**
     * Test case: TC_MOMO_02
     * Test generating MOMO payment URL without voucher
     * Input: Valid amount, MedicalRegistryList, null Voucher
     * Expected output: Map containing resultCode "0" and valid payUrl
     *
     * Mục tiêu: Kiểm tra việc tạo URL thanh toán MOMO không có voucher
     * Đầu vào: Số tiền hợp lệ, đối tượng MedicalRegistryList, Voucher là null
     * Đầu ra mong đợi: Map chứa resultCode "0" và payUrl hợp lệ
     */
    @Test
    @DisplayName("TC_MOMO_02: Generate MOMO payment URL without voucher")
    void testGenerateMOMOUrlPayment_WithoutVoucher() {
        // Arrange
        ResponseEntity<Map> responseEntity = new ResponseEntity<>(momoResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(responseEntity);

        // Act
        Map<String, Object> result = paymentMOMODetailService.generateMOMOUrlPayment(amount, mrl, null);

        // Assert
        assertNotNull(result);
        assertEquals("0", result.get("resultCode"));
        assertEquals("https://test-payment.momo.vn/pay/test-url", result.get("payUrl"));
    }

    /**
     * Test case: TC_MOMO_03
     * Test generating MOMO payment URL with medical examination
     * Input: Valid amount, MedicalRegistryList with MedicalExamination, Voucher
     * Expected output: Map containing resultCode "0" and valid payUrl
     *
     * Mục tiêu: Kiểm tra việc tạo URL thanh toán MOMO với medical examination
     * Đầu vào: Số tiền hợp lệ, đối tượng MedicalRegistryList có MedicalExamination, Voucher hợp lệ
     * Đầu ra mong đợi: Map chứa resultCode "0" và payUrl hợp lệ
     */
    @Test
    @DisplayName("TC_MOMO_03: Generate MOMO payment URL with medical examination")
    void testGenerateMOMOUrlPayment_WithMedicalExamination() {
        // Arrange
        MedicalExamination me = new MedicalExamination();
        me.setId(200);
        mrl.setMedicalExamination(me);

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(momoResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(responseEntity);

        // Act
        Map<String, Object> result = paymentMOMODetailService.generateMOMOUrlPayment(amount, mrl, voucher);

        // Assert
        assertNotNull(result);
        assertEquals("0", result.get("resultCode"));
        assertEquals("https://test-payment.momo.vn/pay/test-url", result.get("payUrl"));
    }

    /**
     * Test case: TC_MOMO_04
     * Test generating MOMO payment URL with error response
     * Input: Valid amount, MedicalRegistryList, Voucher
     * Expected output: Map containing error resultCode
     *
     * Mục tiêu: Kiểm tra xử lý lỗi khi MOMO trả về response lỗi
     * Đầu vào: Số tiền hợp lệ, đối tượng MedicalRegistryList, Voucher hợp lệ
     * Đầu ra mong đợi: Map chứa resultCode lỗi và thông báo lỗi
     */
    @Test
    @DisplayName("TC_MOMO_04: Generate MOMO payment URL with error response")
    void testGenerateMOMOUrlPayment_ErrorResponse() {
        // Arrange
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("resultCode", "99");
        errorResponse.put("message", "Error processing payment");

        ResponseEntity<Map> responseEntity = new ResponseEntity<>(errorResponse, HttpStatus.OK);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(responseEntity);

        // Act
        Map<String, Object> result = paymentMOMODetailService.generateMOMOUrlPayment(amount, mrl, voucher);

        // Assert
        assertNotNull(result);
        assertEquals("99", result.get("resultCode"));
        assertEquals("Error processing payment", result.get("message"));
    }
}
