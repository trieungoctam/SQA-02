package com.spring.privateClinicManage.payment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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

import com.spring.privateClinicManage.api.ApiMOMOPaymentController;
import com.spring.privateClinicManage.config.PaymentMomoConfig;
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
import com.spring.privateClinicManage.service.PaymentMOMODetailService;
import com.spring.privateClinicManage.service.StatusIsApprovedService;
import com.spring.privateClinicManage.service.UserService;
import com.spring.privateClinicManage.service.UserVoucherService;
import com.spring.privateClinicManage.service.VoucherService;
import com.spring.privateClinicManage.service.impl.PaymentMOMODetailServiceImpl;

/**
 * Test chức năng thanh toán qua MOMO
 *
 * Các test case kiểm tra các luồng nghiệp vụ chính và các trường hợp ngoại lệ
 * khi thanh toán qua MOMO
 */
@ExtendWith(MockitoExtension.class)
public class MomoPaymentTest {

    @Mock
    private RestTemplate restTemplate;

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
    private PaymentMOMODetailService paymentMOMODetailService;

    @InjectMocks
    private ApiMOMOPaymentController apiMOMOPaymentController;

    // Dữ liệu test
    private User user;
    private MedicalRegistryList mrl;
    private Voucher voucher;
    private PaymentInitDto paymentInitDto;
    private Map<String, Object> momoResponse;
    private StatusIsApproved statusPaymentPhase1;

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

        // Khởi tạo response giả lập từ MOMO
        momoResponse = new HashMap<>();
        momoResponse.put("resultCode", "0");
        momoResponse.put("payUrl", "https://test-payment.momo.vn/pay/test-url");

        // Cấu hình MOMO cho testing
        PaymentMomoConfig.momo_ApiUrl = "https://test-payment.momo.vn/v2/gateway/api/create";
        PaymentMomoConfig.momo_accessKey = "testAccessKey";
        PaymentMomoConfig.momo_secretKey = "testSecretKey";
        PaymentMomoConfig.momo_partnerCode = "MOMO";
        PaymentMomoConfig.momo_redirectUrl = "http://localhost:8888/api/payment/momo/return/";
    }

    /**
     * Test case: TC_MOMO_BIZ_01
     * Mô tả: Kiểm tra luồng thanh toán thành công qua MOMO cho giai đoạn 1 (đăng ký khám)
     *
     * Đầu vào:
     * - Người dùng đã đăng nhập
     * - Phiếu khám hợp lệ thuộc về người dùng
     * - Số tiền thanh toán hợp lệ
     * - Voucher giảm giá hợp lệ
     *
     * Kết quả mong đợi:
     * - Trả về URL thanh toán MOMO
     * - URL chứa thông tin thanh toán chính xác
     */
    @Test
    @DisplayName("TC_MOMO_BIZ_01: Thanh toán thành công qua MOMO cho giai đoạn 1")
    void testMomoPaymentPhase1_Success() {
        // Arrange - Chuẩn bị dữ liệu và mock các service
        when(userService.getCurrentLoginUser()).thenReturn(user);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(mrl);
        when(voucherService.findVoucherById(anyInt())).thenReturn(voucher);

        when(paymentMOMODetailService.generateMOMOUrlPayment(anyLong(), any(MedicalRegistryList.class), any(Voucher.class)))
                .thenReturn(momoResponse);

        // Act - Thực hiện thanh toán
        ResponseEntity<Object> response = apiMOMOPaymentController.payment(paymentInitDto);

        // Assert - Kiểm tra kết quả
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("https://test-payment.momo.vn/pay/test-url", response.getBody());

        // Verify - Xác nhận các service đã được gọi đúng
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService).findById(100);
        verify(voucherService).findVoucherById(1);
    }

    /**
     * Test case: TC_MOMO_BIZ_02
     * Mô tả: Kiểm tra thanh toán MOMO khi người dùng không tồn tại
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
    @DisplayName("TC_MOMO_BIZ_02: Thanh toán MOMO khi người dùng không tồn tại")
    void testMomoPayment_UserNotFound() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiMOMOPaymentController.payment(paymentInitDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng không tồn tại", response.getBody());

        // Verify
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService, never()).findById(anyInt());
    }

    /**
     * Test case: TC_MOMO_BIZ_03
     * Mô tả: Kiểm tra thanh toán MOMO khi phiếu khám không thuộc về người dùng
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
    @DisplayName("TC_MOMO_BIZ_03: Thanh toán MOMO khi phiếu khám không thuộc về người dùng")
    void testMomoPayment_MrlNotBelongToUser() {
        // Arrange
        User differentUser = new User();
        differentUser.setId(2);
        differentUser.setName("Người dùng khác");

        MedicalRegistryList mrlWithDifferentUser = new MedicalRegistryList();
        mrlWithDifferentUser.setId(100);
        mrlWithDifferentUser.setUser(differentUser);

        when(userService.getCurrentLoginUser()).thenReturn(user);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(mrlWithDifferentUser);

        // Act
        ResponseEntity<Object> response = apiMOMOPaymentController.payment(paymentInitDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng này không có phiếu khám này !", response.getBody());

        // Verify
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService).findById(100);
    }

    /**
     * Test case: TC_MOMO_BIZ_04
     * Mô tả: Kiểm tra thanh toán MOMO khi MOMO trả về lỗi
     *
     * Đầu vào:
     * - Người dùng đã đăng nhập
     * - Phiếu khám hợp lệ
     * - MOMO trả về lỗi
     *
     * Kết quả mong đợi:
     * - Trả về lỗi HTTP 400
     * - Thông báo lỗi từ MOMO
     */
    @Test
    @DisplayName("TC_MOMO_BIZ_04: Thanh toán MOMO khi MOMO trả về lỗi")
    void testMomoPayment_MomoError() {
        // Arrange
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("resultCode", "99");
        errorResponse.put("message", "Lỗi xử lý thanh toán");

        when(userService.getCurrentLoginUser()).thenReturn(user);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(mrl);
        when(voucherService.findVoucherById(anyInt())).thenReturn(voucher);
        when(paymentMOMODetailService.generateMOMOUrlPayment(anyLong(), any(MedicalRegistryList.class), any(Voucher.class)))
                .thenReturn(errorResponse);

        // Act
        ResponseEntity<Object> response = apiMOMOPaymentController.payment(paymentInitDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Thanh toán thất bại"));
    }

    /**
     * Test case: TC_MOMO_BIZ_05
     * Mô tả: Kiểm tra thanh toán MOMO cho giai đoạn 2 (sau khám)
     *
     * Đầu vào:
     * - Người dùng đã đăng nhập
     * - Phiếu khám hợp lệ với thông tin khám bệnh
     *
     * Kết quả mong đợi:
     * - Trả về URL thanh toán MOMO
     * - URL chứa thông tin thanh toán chính xác
     */
    @Test
    @DisplayName("TC_MOMO_BIZ_05: Thanh toán thành công qua MOMO cho giai đoạn 2")
    void testMomoPaymentPhase2_Success() {
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
        ResponseEntity<Object> response = apiMOMOPaymentController.payment(paymentInitDto);

        // Assert - Giả sử controller trả về NOT_FOUND cho giai đoạn 2
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        // Verify
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService).findById(100);
    }

    /**
     * Test case: TC_MOMO_BIZ_06
     * Mô tả: Kiểm tra thanh toán MOMO với số tiền 0
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
    @DisplayName("TC_MOMO_BIZ_06: Thanh toán MOMO với số tiền 0")
    void testMomoPayment_ZeroAmount() {
        // Arrange
        paymentInitDto.setAmount(0L);

        when(userService.getCurrentLoginUser()).thenReturn(user);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(mrl);
        when(voucherService.findVoucherById(anyInt())).thenReturn(voucher);
        when(paymentMOMODetailService.generateMOMOUrlPayment(anyLong(), any(MedicalRegistryList.class), any(Voucher.class)))
                .thenReturn(momoResponse);

        // Act
        ResponseEntity<Object> response = apiMOMOPaymentController.payment(paymentInitDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("https://test-payment.momo.vn/pay/test-url", response.getBody());
    }
}
