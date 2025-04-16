package com.spring.privateClinicManage.payment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.spring.privateClinicManage.api.ApiYtaRestController;
import com.spring.privateClinicManage.dto.CashPaymentDto;
import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.service.DownloadPDFService;
import com.spring.privateClinicManage.service.MailSenderService;
import com.spring.privateClinicManage.service.MedicalExaminationService;
import com.spring.privateClinicManage.service.MedicalRegistryListService;
import com.spring.privateClinicManage.service.PaymentDetailPhase1Service;
import com.spring.privateClinicManage.service.PaymentDetailPhase2Service;
import com.spring.privateClinicManage.service.StatusIsApprovedService;
import com.spring.privateClinicManage.service.UserService;

/**
 * Test chức năng thanh toán bằng tiền mặt
 * 
 * Các test case kiểm tra các luồng nghiệp vụ chính và các trường hợp ngoại lệ
 * khi thanh toán bằng tiền mặt
 */
@ExtendWith(MockitoExtension.class)
public class CashPaymentTest {

    @Mock
    private UserService userService;

    @Mock
    private MedicalRegistryListService medicalRegistryListService;

    @Mock
    private PaymentDetailPhase1Service paymentDetailPhase1Service;

    @Mock
    private PaymentDetailPhase2Service paymentDetailPhase2Service;

    @Mock
    private StatusIsApprovedService statusIsApprovedService;

    @Mock
    private MedicalExaminationService medicalExaminationService;

    @Mock
    private DownloadPDFService downloadPDFService;
    
    @Mock
    private MailSenderService mailSenderService;
    
    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ApiYtaRestController apiYtaRestController;

    // Dữ liệu test
    private User user;
    private MedicalRegistryList mrl;
    private MedicalExamination me;
    private StatusIsApproved statusPaymentPhase1;
    private StatusIsApproved statusPaymentPhase2;
    private StatusIsApproved statusSuccess;
    private StatusIsApproved statusFinished;
    private CashPaymentDto cashPaymentDto;

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

        // Khởi tạo các trạng thái
        statusPaymentPhase1 = new StatusIsApproved();
        statusPaymentPhase1.setId(1);
        statusPaymentPhase1.setStatus("PAYMENTPHASE1");

        statusPaymentPhase2 = new StatusIsApproved();
        statusPaymentPhase2.setId(2);
        statusPaymentPhase2.setStatus("PAYMENTPHASE2");

        statusSuccess = new StatusIsApproved();
        statusSuccess.setId(3);
        statusSuccess.setStatus("SUCCESS");

        statusFinished = new StatusIsApproved();
        statusFinished.setId(4);
        statusFinished.setStatus("FINISHED");

        // Khởi tạo phiếu đăng ký khám
        mrl = new MedicalRegistryList();
        mrl.setId(100);
        mrl.setUser(user);
        mrl.setStatusIsApproved(statusPaymentPhase1);
        mrl.setIsCanceled(false);

        // Khởi tạo thông tin khám bệnh
        me = new MedicalExamination();
        me.setId(200);
        me.setMrl(mrl);
        me.setFollowUpDate(null);

        // Khởi tạo dữ liệu thanh toán tiền mặt
        cashPaymentDto = new CashPaymentDto();
        cashPaymentDto.setMrlId(100);
        cashPaymentDto.setAmount(100000L);
    }

    /**
     * Test case: TC_CASH_BIZ_01
     * Mô tả: Kiểm tra thanh toán tiền mặt thành công cho giai đoạn 1 (đăng ký khám)
     * 
     * Đầu vào:
     * - Người dùng đã đăng nhập
     * - Phiếu khám hợp lệ ở trạng thái PAYMENTPHASE1
     * - Số tiền thanh toán hợp lệ
     * 
     * Kết quả mong đợi:
     * - Trả về HTTP 200
     * - Thông báo "Thanh toán thành công !"
     * - Trạng thái phiếu khám được cập nhật thành SUCCESS
     */
    @Test
    @DisplayName("TC_CASH_BIZ_01: Thanh toán tiền mặt thành công cho giai đoạn 1")
    void testCashPaymentPhase1_Success() throws Exception {
        // Arrange - Chuẩn bị dữ liệu và mock các service
        when(userService.getCurrentLoginUser()).thenReturn(user);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(mrl);
        when(statusIsApprovedService.findByStatus("SUCCESS")).thenReturn(statusSuccess);
        doNothing().when(paymentDetailPhase1Service).savePdp1(any());
        doNothing().when(medicalRegistryListService).createQRCodeAndUpLoadCloudinaryAndSetStatus(any(), any());
        doNothing().when(mailSenderService).sendStatusRegisterEmail(any(), anyString(), any());
        doNothing().when(messagingTemplate).convertAndSend(eq("/notify/cashSuccesfully/" + user.getId()), any(MedicalRegistryList.class));

        // Act - Thực hiện thanh toán
        ResponseEntity<Object> response = apiYtaRestController.cashPaymentMrl(cashPaymentDto);

        // Assert - Kiểm tra kết quả
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Thanh toán thành công !", response.getBody());
        
        // Verify - Xác nhận các service đã được gọi đúng
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService).findById(100);
        verify(paymentDetailPhase1Service).savePdp1(any());
        verify(statusIsApprovedService).findByStatus("SUCCESS");
        verify(medicalRegistryListService).createQRCodeAndUpLoadCloudinaryAndSetStatus(eq(mrl), eq(statusSuccess));
    }

    /**
     * Test case: TC_CASH_BIZ_02
     * Mô tả: Kiểm tra thanh toán tiền mặt thành công cho giai đoạn 2 (sau khám)
     * 
     * Đầu vào:
     * - Người dùng đã đăng nhập
     * - Phiếu khám hợp lệ ở trạng thái PAYMENTPHASE2
     * - Phiếu khám có thông tin khám bệnh
     * - Số tiền thanh toán hợp lệ
     * 
     * Kết quả mong đợi:
     * - Trả về HTTP 200
     * - Thông báo "Thanh toán thành công !"
     * - Trạng thái phiếu khám được cập nhật thành FINISHED
     */
    @Test
    @DisplayName("TC_CASH_BIZ_02: Thanh toán tiền mặt thành công cho giai đoạn 2")
    void testCashPaymentPhase2_Success() throws Exception {
        // Arrange
        mrl.setStatusIsApproved(statusPaymentPhase2);
        mrl.setMedicalExamination(me);
        
        when(userService.getCurrentLoginUser()).thenReturn(user);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(mrl);
        when(statusIsApprovedService.findByStatus("FINISHED")).thenReturn(statusFinished);
        doNothing().when(paymentDetailPhase2Service).savePdp2(any());
        doNothing().when(mailSenderService).sendStatusRegisterEmail(any(), anyString(), any());
        doNothing().when(messagingTemplate).convertAndSend(eq("/notify/cashSuccesfully/" + user.getId()), any(MedicalRegistryList.class));

        // Act
        ResponseEntity<Object> response = apiYtaRestController.cashPaymentMrl(cashPaymentDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Thanh toán thành công !", response.getBody());
        
        // Verify
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService).findById(100);
        verify(paymentDetailPhase2Service).savePdp2(any());
        verify(statusIsApprovedService).findByStatus("FINISHED");
    }

    /**
     * Test case: TC_CASH_BIZ_03
     * Mô tả: Kiểm tra thanh toán tiền mặt khi người dùng không tồn tại
     * 
     * Đầu vào:
     * - Người dùng không tồn tại hoặc chưa đăng nhập
     * - Phiếu khám và số tiền hợp lệ
     * 
     * Kết quả mong đợi:
     * - Trả về lỗi HTTP 404
     * - Thông báo lỗi chứa "Người dùng không tồn tại"
     */
    @Test
    @DisplayName("TC_CASH_BIZ_03: Thanh toán tiền mặt khi người dùng không tồn tại")
    void testCashPayment_UserNotFound() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(null);
        // The controller calls findById before checking if user is null
        when(medicalRegistryListService.findById(anyInt())).thenReturn(mrl);

        // Act
        ResponseEntity<Object> response = apiYtaRestController.cashPaymentMrl(cashPaymentDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Người dùng không tồn tại"));
        
        // Verify
        verify(userService).getCurrentLoginUser();
        // The controller calls findById even if user is null
        verify(medicalRegistryListService).findById(anyInt());
    }

    /**
     * Test case: TC_CASH_BIZ_04
     * Mô tả: Kiểm tra thanh toán tiền mặt khi phiếu khám không tồn tại
     * 
     * Đầu vào:
     * - Người dùng đã đăng nhập
     * - Phiếu khám không tồn tại
     * 
     * Kết quả mong đợi:
     * - Trả về lỗi HTTP 404
     * - Thông báo lỗi chứa "Người dùng không tồn tại hoặc đơn đăng ký không tồn tại"
     */
    @Test
    @DisplayName("TC_CASH_BIZ_04: Thanh toán tiền mặt khi phiếu khám không tồn tại")
    void testCashPayment_MrlNotFound() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(user);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiYtaRestController.cashPaymentMrl(cashPaymentDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Người dùng không tồn tại hoặc đơn đăng ký không tồn tại"));
        
        // Verify
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService).findById(100);
    }

    /**
     * Test case: TC_CASH_BIZ_05
     * Mô tả: Kiểm tra thanh toán tiền mặt khi trạng thái phiếu khám không hợp lệ
     * 
     * Đầu vào:
     * - Người dùng đã đăng nhập
     * - Phiếu khám tồn tại nhưng có trạng thái không phải PAYMENTPHASE1 hoặc PAYMENTPHASE2
     * 
     * Kết quả mong đợi:
     * - Trả về lỗi HTTP 401
     * - Thông báo "Trạng thái phiếu đăng ký không hợp lệ"
     */
    @Test
    @DisplayName("TC_CASH_BIZ_05: Thanh toán tiền mặt khi trạng thái phiếu khám không hợp lệ")
    void testCashPayment_InvalidStatus() {
        // Arrange
        StatusIsApproved invalidStatus = new StatusIsApproved();
        invalidStatus.setId(5);
        invalidStatus.setStatus("PENDING");
        
        mrl.setStatusIsApproved(invalidStatus);
        
        when(userService.getCurrentLoginUser()).thenReturn(user);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(mrl);

        // Act
        ResponseEntity<Object> response = apiYtaRestController.cashPaymentMrl(cashPaymentDto);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Trạng thái phiếu đăng ký không hợp lệ", response.getBody());
        
        // Verify
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService).findById(100);
        verify(paymentDetailPhase1Service, never()).savePdp1(any());
        verify(paymentDetailPhase2Service, never()).savePdp2(any());
    }

    /**
     * Test case: TC_CASH_BIZ_06
     * Mô tả: Kiểm tra thanh toán tiền mặt với số tiền 0
     * 
     * Đầu vào:
     * - Người dùng đã đăng nhập
     * - Phiếu khám hợp lệ
     * - Số tiền thanh toán là 0
     * 
     * Kết quả mong đợi:
     * - Trả về HTTP 200
     * - Thông báo "Thanh toán thành công !"
     * - Trạng thái phiếu khám được cập nhật
     */
    @Test
    @DisplayName("TC_CASH_BIZ_06: Thanh toán tiền mặt với số tiền 0")
    void testCashPayment_ZeroAmount() throws Exception {
        // Arrange
        cashPaymentDto.setAmount(0L);
        
        when(userService.getCurrentLoginUser()).thenReturn(user);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(mrl);
        when(statusIsApprovedService.findByStatus("SUCCESS")).thenReturn(statusSuccess);
        doNothing().when(paymentDetailPhase1Service).savePdp1(any());
        doNothing().when(medicalRegistryListService).createQRCodeAndUpLoadCloudinaryAndSetStatus(any(), any());
        doNothing().when(mailSenderService).sendStatusRegisterEmail(any(), anyString(), any());
        doNothing().when(messagingTemplate).convertAndSend(eq("/notify/cashSuccesfully/" + user.getId()), any(MedicalRegistryList.class));

        // Act
        ResponseEntity<Object> response = apiYtaRestController.cashPaymentMrl(cashPaymentDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Thanh toán thành công !", response.getBody());
        
        // Verify
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService).findById(100);
        verify(paymentDetailPhase1Service).savePdp1(any());
    }
}
