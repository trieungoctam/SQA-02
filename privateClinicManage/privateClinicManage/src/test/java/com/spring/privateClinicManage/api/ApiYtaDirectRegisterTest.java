package com.spring.privateClinicManage.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.UnsupportedEncodingException;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.annotation.Rollback;

import com.spring.privateClinicManage.dto.CashPaymentDto;
import com.spring.privateClinicManage.dto.DirectRegisterDto;
import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.PaymentDetailPhase1;
import com.spring.privateClinicManage.entity.PaymentDetailPhase2;
import com.spring.privateClinicManage.entity.Role;
import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.service.DownloadPDFService;
import com.spring.privateClinicManage.service.MailSenderService;
import com.spring.privateClinicManage.service.MedicalRegistryListService;
import com.spring.privateClinicManage.service.PaymentDetailPhase1Service;
import com.spring.privateClinicManage.service.PaymentDetailPhase2Service;
import com.spring.privateClinicManage.service.PrescriptionItemsService;
import com.spring.privateClinicManage.service.ScheduleService;
import com.spring.privateClinicManage.service.StatusIsApprovedService;
import com.spring.privateClinicManage.service.UserService;
import com.spring.privateClinicManage.utilities.CalendarFormat;
import com.spring.privateClinicManage.utilities.CalendarFormatUtil;

import jakarta.mail.MessagingException;

/**
 * Unit tests for ApiYtaRestController - Direct Register functionality
 *
 * This test class covers the direct register and cash payment features
 * of the ApiYtaRestController, which are used by nurses to register
 * patients directly at the clinic.
 */
@ExtendWith(MockitoExtension.class)
public class ApiYtaDirectRegisterTest {

    @Mock
    private UserService userService;

    @Mock
    private MailSenderService mailSenderService;

    @Mock
    private Environment environment;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private MedicalRegistryListService medicalRegistryListService;

    @Mock
    private StatusIsApprovedService statusIsApprovedService;

    @Mock
    private DownloadPDFService downloadPDFService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private PaymentDetailPhase1Service paymentDetailPhase1Service;

    @Mock
    private PaymentDetailPhase2Service paymentDetailPhase2Service;

    @Mock
    private PrescriptionItemsService prescriptionItemsService;

    @InjectMocks
    private ApiYtaRestController apiYtaRestController;

    // Test data
    private User currentUser;
    private User registerUser;
    private Schedule testSchedule;
    private Schedule dayOffSchedule;
    private StatusIsApproved checkingStatus;
    private StatusIsApproved paymentPhase1Status;
    private StatusIsApproved paymentPhase2Status;
    private StatusIsApproved successStatus;
    private StatusIsApproved finishedStatus;
    private StatusIsApproved followupStatus;
    private MedicalRegistryList testMrl;
    private DirectRegisterDto validDirectRegisterDto;
    private CashPaymentDto validCashPaymentDto;

    /**
     * Setup test data before each test
     */
    @BeforeEach
    public void setup() {
        // Mock user roles
        Role ytaRole = new Role();
        ytaRole.setId(1);
        ytaRole.setName("ROLE_YTA");

        Role benhnhanRole = new Role();
        benhnhanRole.setId(2);
        benhnhanRole.setName("ROLE_BENHNHAN");

        // Mock users
        currentUser = new User();
        currentUser.setId(1);
        currentUser.setEmail("yta@example.com");
        currentUser.setRole(ytaRole);

        registerUser = new User();
        registerUser.setId(2);
        registerUser.setEmail("patient@example.com");
        registerUser.setRole(benhnhanRole);

        // Mock schedules
        testSchedule = new Schedule();
        testSchedule.setId(1);
        testSchedule.setDate(new Date());
        testSchedule.setIsDayOff(false);

        dayOffSchedule = new Schedule();
        dayOffSchedule.setId(2);
        dayOffSchedule.setDate(new Date());
        dayOffSchedule.setIsDayOff(true);

        // Mock statuses
        checkingStatus = new StatusIsApproved();
        checkingStatus.setId(1);
        checkingStatus.setStatus("CHECKING");

        paymentPhase1Status = new StatusIsApproved();
        paymentPhase1Status.setId(2);
        paymentPhase1Status.setStatus("PAYMENTPHASE1");

        paymentPhase2Status = new StatusIsApproved();
        paymentPhase2Status.setId(3);
        paymentPhase2Status.setStatus("PAYMENTPHASE2");

        successStatus = new StatusIsApproved();
        successStatus.setId(4);
        successStatus.setStatus("SUCCESS");

        finishedStatus = new StatusIsApproved();
        finishedStatus.setId(5);
        finishedStatus.setStatus("FINISHED");

        followupStatus = new StatusIsApproved();
        followupStatus.setId(6);
        followupStatus.setStatus("FOLLOWUP");

        // Mock MedicalRegistryList
        testMrl = new MedicalRegistryList();
        testMrl.setId(1);
        testMrl.setName("Test Patient");
        testMrl.setFavor("Test symptoms");
        testMrl.setCreatedDate(new Date());
        testMrl.setIsCanceled(false);
        testMrl.setUser(registerUser);
        testMrl.setSchedule(testSchedule);
        testMrl.setStatusIsApproved(paymentPhase1Status);

        // Mock DirectRegisterDto
        validDirectRegisterDto = new DirectRegisterDto();
        validDirectRegisterDto.setName("Test Patient");
        validDirectRegisterDto.setEmail("patient@example.com");
        validDirectRegisterDto.setFavor("Test symptoms");

        // Mock CashPaymentDto
        validCashPaymentDto = new CashPaymentDto();
        validCashPaymentDto.setMrlId(1);
        validCashPaymentDto.setAmount(50000L);
    }

    /**
     * TC_DR_01: Test direct register with valid data
     *
     * Input: Valid DirectRegisterDto
     * Expected: HTTP 201 Created
     */
    @Test
    @DisplayName("TC_DR_01: Test direct register with valid data")
    @Rollback(true)
    public void testDirectRegister_ValidData() throws Exception {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(userService.findByEmail(anyString())).thenReturn(registerUser);

        // Mock calendar format
        CalendarFormat calendarFormat = new CalendarFormat();
        calendarFormat.setYear(2023);
        calendarFormat.setMonth(7);
        calendarFormat.setDay(15);

        // Mock schedule service
        when(scheduleService.findByDayMonthYear(anyInt(), anyInt(), anyInt())).thenReturn(testSchedule);

        // Mock count check
        when(medicalRegistryListService.countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved(
                any(User.class), any(Schedule.class), anyBoolean(), any(StatusIsApproved.class)))
                .thenReturn(0);

        // Mock environment property
        when(environment.getProperty("register_schedule_per_day_max")).thenReturn("4");

        // Mock status service
        when(statusIsApprovedService.findByStatus("PAYMENTPHASE1")).thenReturn(paymentPhase1Status);

        // Mock save MRL
        doNothing().when(medicalRegistryListService).saveMedicalRegistryList(any(MedicalRegistryList.class));

        // Mock messaging
        doNothing().when(messagingTemplate).convertAndSend(anyString(), any(MedicalRegistryList.class));

        // Act
        ResponseEntity<Object> response = apiYtaRestController.directRegister(validDirectRegisterDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Đặt lịch trực tiếp thành công"));

        // Verify service calls
        verify(userService).getCurrentLoginUser();
        verify(userService).findByEmail(validDirectRegisterDto.getEmail());
        verify(scheduleService).findByDayMonthYear(anyInt(), anyInt(), anyInt());
        verify(statusIsApprovedService).findByStatus("PAYMENTPHASE1");
        verify(medicalRegistryListService).saveMedicalRegistryList(any(MedicalRegistryList.class));
        verify(messagingTemplate).convertAndSend(anyString(), any(MedicalRegistryList.class));
    }

    /**
     * TC_DR_02: Test direct register when user is not logged in
     *
     * Input: DirectRegisterDto, no current user
     * Expected: HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_DR_02: Test direct register when user is not logged in")
    @Rollback(true)
    public void testDirectRegister_NotLoggedIn() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiYtaRestController.directRegister(validDirectRegisterDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng không tồn tại", response.getBody());

        // Verify service calls
        verify(userService).getCurrentLoginUser();
        verify(userService, never()).findByEmail(anyString());
    }

    /**
     * TC_DR_03: Test direct register when exceeding daily limit
     *
     * Input: DirectRegisterDto, count > 4
     * Expected: HTTP 401 Unauthorized
     */
    @Test
    @DisplayName("TC_DR_03: Test direct register when exceeding daily limit")
    @Rollback(true)
    public void testDirectRegister_ExceedLimit() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(userService.findByEmail(anyString())).thenReturn(registerUser);
        when(scheduleService.findByDayMonthYear(anyInt(), anyInt(), anyInt())).thenReturn(testSchedule);
        when(statusIsApprovedService.findByStatus("CHECKING")).thenReturn(checkingStatus);

        // Mock count check - exceed limit
        when(medicalRegistryListService.countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved(
                any(User.class), any(Schedule.class), anyBoolean(), any(StatusIsApproved.class)))
                .thenReturn(5);

        // Mock environment property
        when(environment.getProperty("register_schedule_per_day_max")).thenReturn("4");

        // Act
        ResponseEntity<Object> response = apiYtaRestController.directRegister(validDirectRegisterDto);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("đã đăng kí hạn mức"));
    }

    /**
     * TC_DR_04: Test direct register with non-existent patient
     *
     * Input: DirectRegisterDto with invalid email
     * Expected: HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_DR_04: Test direct register with non-existent patient")
    @Rollback(true)
    public void testDirectRegister_NonExistentPatient() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(userService.findByEmail(anyString())).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiYtaRestController.directRegister(validDirectRegisterDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng không tồn tại", response.getBody());
    }

    /**
     * TC_DR_05: Test direct register on a day off
     *
     * Input: DirectRegisterDto, schedule with isDayOff=true
     * Expected: HTTP 401 Unauthorized
     */
    @Test
    @DisplayName("TC_DR_05: Test direct register on a day off")
    @Rollback(true)
    public void testDirectRegister_DayOff() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(userService.findByEmail(anyString())).thenReturn(registerUser);
        when(scheduleService.findByDayMonthYear(anyInt(), anyInt(), anyInt())).thenReturn(dayOffSchedule);

        // Act
        ResponseEntity<Object> response = apiYtaRestController.directRegister(validDirectRegisterDto);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("không có lịch làm việc ngày này"));
    }

    /**
     * TC_DR_07: Test direct register with email sending failure
     *
     * Input: Valid DirectRegisterDto but email service throws exception
     * Expected: HTTP 201 Created (should still succeed despite email failure)
     */
    @Test
    @DisplayName("TC_DR_07: Test direct register with email sending failure")
    @Rollback(true)
    public void testDirectRegister_EmailFailure() throws Exception {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(userService.findByEmail(anyString())).thenReturn(registerUser);
        when(scheduleService.findByDayMonthYear(anyInt(), anyInt(), anyInt())).thenReturn(testSchedule);
        when(statusIsApprovedService.findByStatus("PAYMENTPHASE1")).thenReturn(paymentPhase1Status);
        when(medicalRegistryListService.countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved(
                any(User.class), any(Schedule.class), anyBoolean(), any(StatusIsApproved.class)))
                .thenReturn(0);
        when(environment.getProperty("register_schedule_per_day_max")).thenReturn("4");

        // Mock email service to throw exception
        doThrow(new MessagingException("Email sending failed"))
            .when(mailSenderService).sendStatusRegisterEmail(
                any(MedicalRegistryList.class), anyString(), any(StatusIsApproved.class));

        // Act
        ResponseEntity<Object> response = apiYtaRestController.directRegister(validDirectRegisterDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Đặt lịch trực tiếp thành công"));

        // Verify service calls
        verify(medicalRegistryListService).saveMedicalRegistryList(any(MedicalRegistryList.class));
    }

    /**
     * TC_CP_01: Test cash payment for phase 1 (registration payment)
     *
     * Input: Valid CashPaymentDto for PAYMENTPHASE1
     * Expected: HTTP 200 OK
     */
    @Test
    @DisplayName("TC_CP_01: Test cash payment for phase 1")
    @Rollback(true)
    public void testCashPayment_Phase1() throws Exception {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(testMrl);
        when(statusIsApprovedService.findByStatus("SUCCESS")).thenReturn(successStatus);

        // Mock services
        doNothing().when(paymentDetailPhase1Service).savePdp1(any(PaymentDetailPhase1.class));
        doNothing().when(medicalRegistryListService).createQRCodeAndUpLoadCloudinaryAndSetStatus(
                any(MedicalRegistryList.class), any(StatusIsApproved.class));
        doNothing().when(messagingTemplate).convertAndSend(anyString(), any(MedicalRegistryList.class));

        // Act
        ResponseEntity<Object> response = apiYtaRestController.cashPaymentMrl(validCashPaymentDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Thanh toán thành công !", response.getBody());

        // Verify service calls
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService).findById(validCashPaymentDto.getMrlId());
        verify(paymentDetailPhase1Service).savePdp1(any(PaymentDetailPhase1.class));
        verify(medicalRegistryListService).createQRCodeAndUpLoadCloudinaryAndSetStatus(
                any(MedicalRegistryList.class), any(StatusIsApproved.class));
    }

    /**
     * TC_CP_02: Test cash payment for phase 2 (medical examination payment)
     *
     * Input: Valid CashPaymentDto for PAYMENTPHASE2
     * Expected: HTTP 200 OK
     */
    @Test
    @DisplayName("TC_CP_02: Test cash payment for phase 2")
    @Rollback(true)
    public void testCashPayment_Phase2() throws Exception {
        // Arrange
        // Set up MRL with PAYMENTPHASE2 status
        testMrl.setStatusIsApproved(paymentPhase2Status);

        // Create medical examination
        MedicalExamination medicalExamination = new MedicalExamination();
        medicalExamination.setId(1);
        medicalExamination.setMrl(testMrl);
        medicalExamination.setFollowUpDate(null); // No follow-up needed
        testMrl.setMedicalExamination(medicalExamination);

        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(testMrl);
        when(statusIsApprovedService.findByStatus("FINISHED")).thenReturn(finishedStatus);

        // Mock services
        doNothing().when(paymentDetailPhase2Service).savePdp2(any(PaymentDetailPhase2.class));
        doNothing().when(messagingTemplate).convertAndSend(anyString(), any(MedicalRegistryList.class));

        // Act
        ResponseEntity<Object> response = apiYtaRestController.cashPaymentMrl(validCashPaymentDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Thanh toán thành công !", response.getBody());

        // Verify service calls
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService).findById(validCashPaymentDto.getMrlId());
        verify(paymentDetailPhase2Service).savePdp2(any(PaymentDetailPhase2.class));
        verify(statusIsApprovedService).findByStatus("FINISHED");
    }

    /**
     * TC_CP_03: Test cash payment with non-existent MRL
     *
     * Input: CashPaymentDto with invalid MRL ID
     * Expected: HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_CP_03: Test cash payment with non-existent MRL")
    @Rollback(true)
    public void testCashPayment_NonExistentMRL() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiYtaRestController.cashPaymentMrl(validCashPaymentDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("không tồn tại"));
    }

    /**
     * TC_CP_04: Test cash payment with invalid status
     *
     * Input: CashPaymentDto for MRL with invalid status
     * Expected: HTTP 401 Unauthorized
     */
    @Test
    @DisplayName("TC_CP_04: Test cash payment with invalid status")
    @Rollback(true)
    public void testCashPayment_InvalidStatus() {
        // Arrange
        // Set up MRL with CHECKING status (not valid for payment)
        testMrl.setStatusIsApproved(checkingStatus);

        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(testMrl);

        // Act
        ResponseEntity<Object> response = apiYtaRestController.cashPaymentMrl(validCashPaymentDto);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Trạng thái phiếu đăng ký không hợp lệ", response.getBody());
    }

    /**
     * TC_CP_05: Test cash payment with follow-up needed
     *
     * Input: CashPaymentDto for MRL with follow-up date
     * Expected: HTTP 200 OK, status set to FOLLOWUP
     */
    @Test
    @DisplayName("TC_CP_05: Test cash payment with follow-up needed")
    @Rollback(true)
    public void testCashPayment_WithFollowUp() throws Exception {
        // Arrange
        // Set up MRL with PAYMENTPHASE2 status
        testMrl.setStatusIsApproved(paymentPhase2Status);

        // Create medical examination with follow-up date
        MedicalExamination medicalExamination = new MedicalExamination();
        medicalExamination.setId(1);
        medicalExamination.setMrl(testMrl);
        medicalExamination.setFollowUpDate(new Date()); // Follow-up needed
        testMrl.setMedicalExamination(medicalExamination);

        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(testMrl);
        when(statusIsApprovedService.findByStatus("FOLLOWUP")).thenReturn(followupStatus);

        // Mock services
        doNothing().when(paymentDetailPhase2Service).savePdp2(any(PaymentDetailPhase2.class));
        doNothing().when(messagingTemplate).convertAndSend(anyString(), any(MedicalRegistryList.class));

        // Act
        ResponseEntity<Object> response = apiYtaRestController.cashPaymentMrl(validCashPaymentDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Thanh toán thành công !", response.getBody());

        // Verify service calls
        verify(userService).getCurrentLoginUser();
        verify(medicalRegistryListService).findById(validCashPaymentDto.getMrlId());
        verify(paymentDetailPhase2Service).savePdp2(any(PaymentDetailPhase2.class));
        verify(statusIsApprovedService).findByStatus("FOLLOWUP");
    }
}
