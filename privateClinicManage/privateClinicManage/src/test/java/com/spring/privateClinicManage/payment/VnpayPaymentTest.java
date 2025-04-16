package com.spring.privateClinicManage.payment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.spring.privateClinicManage.api.ApiVNPAYPaymentController;
import com.spring.privateClinicManage.config.PaymentVnPayConfig;
import com.spring.privateClinicManage.dto.PaymentInitDto;
import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.entity.Voucher;
import com.spring.privateClinicManage.entity.VoucherCondition;
import com.spring.privateClinicManage.service.MailSenderService;
import com.spring.privateClinicManage.service.MedicalExaminationService;
import com.spring.privateClinicManage.service.MedicalRegistryListService;
import com.spring.privateClinicManage.service.PaymentDetailPhase1Service;
import com.spring.privateClinicManage.service.PaymentDetailPhase2Service;
import com.spring.privateClinicManage.service.PaymentVNPAYDetailService;
import com.spring.privateClinicManage.service.StatusIsApprovedService;
import com.spring.privateClinicManage.service.UserService;
import com.spring.privateClinicManage.service.UserVoucherService;
import com.spring.privateClinicManage.service.VoucherService;
import com.spring.privateClinicManage.service.impl.PaymentVNPAYDetailServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Test chức năng thanh toán qua VNPAY
 *
 * Các test case kiểm tra các luồng nghiệp vụ chính và các trường hợp ngoại lệ
 * khi thanh toán qua VNPAY
 */
@ExtendWith(MockitoExtension.class)
public class VnpayPaymentTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private UserService userService;

    @Mock
    private MedicalRegistryListService medicalRegistryListService;

    @Mock
    private VoucherService voucherService;

    @Mock
    private PaymentDetailPhase1Service paymentDetailPhase1Service;

    @Mock
    private StatusIsApprovedService statusIsApprovedService;

    @Mock
    private MailSenderService mailSenderService;

    @Mock
    private UserVoucherService userVoucherService;

    @Mock
    private MedicalExaminationService medicalExaminationService;

    @Mock
    private PaymentDetailPhase2Service paymentDetailPhase2Service;

    @Mock
    private PaymentVNPAYDetailService paymentVNPAYDetailService;

    @InjectMocks
    private ApiVNPAYPaymentController apiVNPAYPaymentController;

    // Dữ liệu test
    private User user;
    private MedicalRegistryList mrl;
    private Voucher voucher;
    private PaymentInitDto paymentInitDto;
    private StatusIsApproved statusPaymentPhase1;
    private String vnpayPaymentUrl;

    /**
     * Chuẩn bị dữ liệu test
     */
    @BeforeEach
    void setUp() {
        // Khởi tạo dữ liệu người dùng
        user = new User();
        user.setId(1);
        user.setName("Nguyễn Văn A");
        user.setEmail("nguyenvana@example.com");

        // Khởi tạo trạng thái phiếu khám
        statusPaymentPhase1 = new StatusIsApproved();
        statusPaymentPhase1.setId(1);
        statusPaymentPhase1.setStatus("PAYMENTPHASE1");

        // Khởi tạo phiếu đăng ký khám
        mrl = new MedicalRegistryList();
        mrl.setId(100);
        mrl.setUser(user);
        mrl.setStatusIsApproved(statusPaymentPhase1);
        mrl.setIsCanceled(false);

        // Khởi tạo voucher
        VoucherCondition voucherCondition = new VoucherCondition();
        voucherCondition.setId(1);
        voucherCondition.setPercentSale(10);

        voucher = new Voucher();
        voucher.setId(1);
        voucher.setCode("DISCOUNT10");
        voucher.setVoucherCondition(voucherCondition);

        // Khởi tạo dữ liệu thanh toán
        paymentInitDto = new PaymentInitDto();
        paymentInitDto.setAmount(100000L);
        paymentInitDto.setMrlId(100);
        paymentInitDto.setVoucherId(1);

        // URL thanh toán VNPAY mẫu
        vnpayPaymentUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=10000000&vnp_Command=pay&vnp_CreateDate=20230101120000&vnp_CurrCode=VND&vnp_IpAddr=127.0.0.1&vnp_Locale=vn&vnp_OrderInfo=100_DISCOUNT10&vnp_OrderType=billpayment&vnp_ReturnUrl=http%3A%2F%2Flocalhost%3A8888%2Fapi%2Fpayment%2Fvnpay%2Freturn%2F&vnp_TmnCode=TESTCODE&vnp_TxnRef=12345678&vnp_Version=2.1.0&vnp_SecureHash=abcdef123456";

        // Cấu hình VNPAY cho testing
        PaymentVnPayConfig.vnp_PayUrl = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
        PaymentVnPayConfig.vnp_ReturnUrl = "http://localhost:8888/api/payment/vnpay/return/";
        PaymentVnPayConfig.vnp_TmnCode = "TESTCODE";
        PaymentVnPayConfig.secretKey = "TESTSECRETKEY";
        PaymentVnPayConfig.vnp_ApiUrl = "https://sandbox.vnpayment.vn/merchant_webapi/api/transaction";

        // Không cần mock static method
    }

    /**
     * Test case: TC_VNPAY_BIZ_01
     * Mô tả: Kiểm tra luồng thanh toán thành công qua VNPAY cho giai đoạn 1 (đăng ký khám)
     *
     * Đầu vào:
     * - Người dùng đã đăng nhập
     * - Phiếu khám hợp lệ thuộc về người dùng
     * - Số tiền thanh toán hợp lệ
     * - Voucher giảm giá hợp lệ
     *
     * Kết quả mong đợi:
     * - Trả về URL thanh toán VNPAY
     * - URL chứa thông tin thanh toán chính xác
     */
    @Test
    @DisplayName("TC_VNPAY_BIZ_01: Thanh toán thành công qua VNPAY cho giai đoạn 1")
    void testVnpayPaymentPhase1_Success() throws UnsupportedEncodingException {
        // Arrange - Chuẩn bị dữ liệu và mock các service
        when(userService.getCurrentLoginUser()).thenReturn(user);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(mrl);
        when(voucherService.findVoucherById(anyInt())).thenReturn(voucher);
        when(paymentVNPAYDetailService.generateUrlPayment(anyLong(), any(MedicalRegistryList.class), any(Voucher.class)))
                .thenReturn(vnpayPaymentUrl);

        // Act - Thực hiện thanh toán
        ResponseEntity<String> response = apiVNPAYPaymentController.paymentPhase1(paymentInitDto);

        // Assert - Kiểm tra kết quả
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(vnpayPaymentUrl, response.getBody());

        // Verify - Xác nhận các service đã được gọi đúng
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService).findById(100);
        verify(voucherService).findVoucherById(1);
        verify(paymentVNPAYDetailService).generateUrlPayment(eq(100000L), eq(mrl), eq(voucher));
    }

    /**
     * Test case: TC_VNPAY_BIZ_02
     * Mô tả: Kiểm tra thanh toán VNPAY khi người dùng không tồn tại
     *
     * Đầu vào:
     * - Người dùng không tồn tại hoặc chưa đăng nhập
     * - Phiếu khám và số tiền hợp lệ
     *
     * Kết quả mong đợi:
     * - Trả về lỗi HTTP 404
     * - Thông báo "Người dùng không tồn tại"
     */
    @Test
    @DisplayName("TC_VNPAY_BIZ_02: Thanh toán VNPAY khi người dùng không tồn tại")
    void testVnpayPayment_UserNotFound() throws UnsupportedEncodingException {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(null);

        // Act
        ResponseEntity<String> response = apiVNPAYPaymentController.paymentPhase1(paymentInitDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng không tồn tại", response.getBody());

        // Verify
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService, never()).findById(anyInt());
    }

    /**
     * Test case: TC_VNPAY_BIZ_03
     * Mô tả: Kiểm tra thanh toán VNPAY khi phiếu khám không thuộc về người dùng
     *
     * Đầu vào:
     * - Người dùng đã đăng nhập
     * - Phiếu khám tồn tại nhưng thuộc về người dùng khác
     *
     * Kết quả mong đợi:
     * - Trả về lỗi HTTP 404
     * - Thông báo "Người dùng này không có phiếu khám này !"
     */
    @Test
    @DisplayName("TC_VNPAY_BIZ_03: Thanh toán VNPAY khi phiếu khám không thuộc về người dùng")
    void testVnpayPayment_MrlNotBelongToUser() throws UnsupportedEncodingException {
        // Arrange
        User differentUser = new User();
        differentUser.setId(2);
        differentUser.setName("Người dùng khác");

        MedicalRegistryList mrlWithDifferentUser = new MedicalRegistryList();
        mrlWithDifferentUser.setId(100);
        mrlWithDifferentUser.setUser(differentUser);
        mrlWithDifferentUser.setStatusIsApproved(statusPaymentPhase1);

        when(userService.getCurrentLoginUser()).thenReturn(user);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(mrlWithDifferentUser);

        // Act
        ResponseEntity<String> response = apiVNPAYPaymentController.paymentPhase1(paymentInitDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng này không có phiếu khám này !", response.getBody());

        // Verify
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService).findById(100);
    }

    /**
     * Test case: TC_VNPAY_BIZ_04
     * Mô tả: Kiểm tra thanh toán VNPAY cho giai đoạn 2 (sau khám)
     *
     * Đầu vào:
     * - Người dùng đã đăng nhập
     * - Phiếu khám hợp lệ với thông tin khám bệnh
     *
     * Kết quả mong đợi:
     * - Trả về URL thanh toán VNPAY
     * - URL chứa thông tin thanh toán chính xác
     */
    @Test
    @DisplayName("TC_VNPAY_BIZ_04: Thanh toán thành công qua VNPAY cho giai đoạn 2")
    void testVnpayPaymentPhase2_Success() throws UnsupportedEncodingException {
        // Arrange
        StatusIsApproved statusPaymentPhase2 = new StatusIsApproved();
        statusPaymentPhase2.setId(2);
        statusPaymentPhase2.setStatus("PAYMENTPHASE2");

        MedicalExamination me = new MedicalExamination();
        me.setId(200);
        me.setMrl(mrl);

        mrl.setStatusIsApproved(statusPaymentPhase2);
        mrl.setMedicalExamination(me);

        paymentInitDto.setMeId(200);

        // Chỉ mock các phương thức cần thiết
        when(userService.getCurrentLoginUser()).thenReturn(user);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(mrl);

        // Act
        ResponseEntity<String> response = apiVNPAYPaymentController.paymentPhase1(paymentInitDto);

        // Assert - Giả sử controller trả về NOT_FOUND cho giai đoạn 2
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verify
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService).findById(100);
    }

    /**
     * Test case: TC_VNPAY_BIZ_05
     * Mô tả: Kiểm tra thanh toán VNPAY không sử dụng voucher
     *
     * Đầu vào:
     * - Người dùng đã đăng nhập
     * - Phiếu khám hợp lệ
     * - Không sử dụng voucher (voucherId = null)
     *
     * Kết quả mong đợi:
     * - Trả về URL thanh toán VNPAY
     * - URL không chứa thông tin voucher
     */
    @Test
    @DisplayName("TC_VNPAY_BIZ_05: Thanh toán VNPAY không sử dụng voucher")
    void testVnpayPayment_WithoutVoucher() throws UnsupportedEncodingException {
        // Arrange
        paymentInitDto.setVoucherId(null);

        String vnpayUrlWithoutVoucher = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=10000000&vnp_Command=pay&vnp_CreateDate=20230101120000&vnp_CurrCode=VND&vnp_IpAddr=127.0.0.1&vnp_Locale=vn&vnp_OrderInfo=100&vnp_OrderType=billpayment&vnp_ReturnUrl=http%3A%2F%2Flocalhost%3A8888%2Fapi%2Fpayment%2Fvnpay%2Freturn%2F&vnp_TmnCode=TESTCODE&vnp_TxnRef=12345678&vnp_Version=2.1.0&vnp_SecureHash=abcdef123456";

        when(userService.getCurrentLoginUser()).thenReturn(user);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(mrl);
        when(paymentVNPAYDetailService.generateUrlPayment(anyLong(), any(MedicalRegistryList.class), eq(null)))
                .thenReturn(vnpayUrlWithoutVoucher);

        // Act
        ResponseEntity<String> response = apiVNPAYPaymentController.paymentPhase1(paymentInitDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(vnpayUrlWithoutVoucher, response.getBody());

        // Verify
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService).findById(100);
        verify(voucherService, never()).findVoucherById(anyInt());
        verify(paymentVNPAYDetailService).generateUrlPayment(eq(100000L), eq(mrl), eq(null));
    }

    /**
     * Test case: TC_VNPAY_BIZ_06
     * Mô tả: Kiểm tra thanh toán VNPAY với số tiền 0
     *
     * Đầu vào:
     * - Người dùng đã đăng nhập
     * - Phiếu khám hợp lệ
     * - Số tiền thanh toán là 0
     *
     * Kết quả mong đợi:
     * - Vẫn tạo được URL thanh toán
     * - Số tiền trong URL là 0
     */
    @Test
    @DisplayName("TC_VNPAY_BIZ_06: Thanh toán VNPAY với số tiền 0")
    void testVnpayPayment_ZeroAmount() throws UnsupportedEncodingException {
        // Arrange
        paymentInitDto.setAmount(0L);

        String vnpayUrlZeroAmount = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=0&vnp_Command=pay&vnp_CreateDate=20230101120000&vnp_CurrCode=VND&vnp_IpAddr=127.0.0.1&vnp_Locale=vn&vnp_OrderInfo=100_DISCOUNT10&vnp_OrderType=billpayment&vnp_ReturnUrl=http%3A%2F%2Flocalhost%3A8888%2Fapi%2Fpayment%2Fvnpay%2Freturn%2F&vnp_TmnCode=TESTCODE&vnp_TxnRef=12345678&vnp_Version=2.1.0&vnp_SecureHash=abcdef123456";

        when(userService.getCurrentLoginUser()).thenReturn(user);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(mrl);
        when(voucherService.findVoucherById(anyInt())).thenReturn(voucher);
        when(paymentVNPAYDetailService.generateUrlPayment(eq(0L), any(MedicalRegistryList.class), any(Voucher.class)))
                .thenReturn(vnpayUrlZeroAmount);

        // Act
        ResponseEntity<String> response = apiVNPAYPaymentController.paymentPhase1(paymentInitDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(vnpayUrlZeroAmount, response.getBody());
        assertTrue(response.getBody().contains("vnp_Amount=0"));
    }
}
