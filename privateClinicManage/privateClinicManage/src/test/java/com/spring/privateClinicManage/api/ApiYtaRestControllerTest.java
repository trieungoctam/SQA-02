package com.spring.privateClinicManage.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import org.springframework.transaction.annotation.Transactional;

import com.spring.privateClinicManage.dto.ConfirmRegisterDto;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.service.MailSenderService;
import com.spring.privateClinicManage.service.MedicalRegistryListService;
import com.spring.privateClinicManage.service.ScheduleService;
import com.spring.privateClinicManage.service.StatusIsApprovedService;
import com.spring.privateClinicManage.service.UserService;

/**
 * Unit Test for ApiYtaRestController
 * This test class focuses on the "Duyệt phiếu đăng ký khám bệnh" functionality
 */
@ExtendWith(MockitoExtension.class)
@Transactional
public class ApiYtaRestControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private StatusIsApprovedService statusIsApprovedService;

    @Mock
    private MedicalRegistryListService medicalRegistryListService;

    @Mock
    private MailSenderService mailSenderService;

    @Mock
    private Environment environment;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ApiYtaRestController apiYtaRestController;

    // Test data
    private User currentUser;
    private Schedule testSchedule;
    private StatusIsApproved checkingStatus;
    private StatusIsApproved approvedStatus;
    private List<MedicalRegistryList> medicalRegistryLists;
    private ConfirmRegisterDto confirmRegisterDto;

    /**
     * Setup test data before each test
     */
    @BeforeEach
    public void setup() {
        // Mock user
        currentUser = new User();
        currentUser.setId(1);
        currentUser.setEmail("test@example.com");

        // Mock schedule
        testSchedule = new Schedule();
        testSchedule.setId(1);
        testSchedule.setDate(new Date());
        testSchedule.setIsDayOff(false);

        // Mock status
        checkingStatus = new StatusIsApproved();
        checkingStatus.setId(1);
        checkingStatus.setStatus("CHECKING");

        approvedStatus = new StatusIsApproved();
        approvedStatus.setId(2);
        approvedStatus.setStatus("PAYMENTPHASE1");

        // Mock medical registry lists
        medicalRegistryLists = new ArrayList<>();
        MedicalRegistryList mrl1 = new MedicalRegistryList();
        mrl1.setId(1);
        mrl1.setName("Patient 1");
        mrl1.setStatusIsApproved(checkingStatus);
        mrl1.setSchedule(testSchedule);
        mrl1.setUser(currentUser);

        MedicalRegistryList mrl2 = new MedicalRegistryList();
        mrl2.setId(2);
        mrl2.setName("Patient 2");
        mrl2.setStatusIsApproved(checkingStatus);
        mrl2.setSchedule(testSchedule);
        mrl2.setUser(currentUser);

        medicalRegistryLists.add(mrl1);
        medicalRegistryLists.add(mrl2);

        // Mock DTO
        confirmRegisterDto = new ConfirmRegisterDto();
        confirmRegisterDto.setStatus("PAYMENTPHASE1");
        confirmRegisterDto.setRegisterDate(new Date());
        confirmRegisterDto.setEmails(Arrays.asList("test@example.com"));
        confirmRegisterDto.setEmailContent("Test content");
    }

    /**
     * TC01: Test successful approval of registration forms
     *
     * Input: Valid confirmRegisterDto with PAYMENTPHASE1 status and valid emails
     * Expected: Returns HttpStatus.OK with success message
     */
    @Test
    @DisplayName("TC01: Test successful approval of registration forms")
    @Rollback(true)
    public void testAutoConfirmRegisters_Success() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(statusIsApprovedService.findByStatus(anyString())).thenReturn(approvedStatus);

        // Mock Calendar setup
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(confirmRegisterDto.getRegisterDate());

        when(scheduleService.findByDayMonthYear(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH))).thenReturn(testSchedule);

        when(statusIsApprovedService.findByStatus("CHECKING")).thenReturn(checkingStatus);
        when(medicalRegistryListService.findByScheduleAndStatusIsApproved2(any(Schedule.class), any(StatusIsApproved.class)))
                .thenReturn(medicalRegistryLists);

        // Act
        ResponseEntity<Object> response = apiYtaRestController.autoConfirmRegisters(confirmRegisterDto);

        // Assert
        assert response.getStatusCode() == HttpStatus.OK;
        verify(medicalRegistryListService, times(medicalRegistryLists.size())).saveMedicalRegistryList(any(MedicalRegistryList.class));
    }

    /**
     * TC02: Test approval with non-existent user
     *
     * Input: confirmRegisterDto but with null current user
     * Expected: Returns HttpStatus.NOT_FOUND with error message
     */
    @Test
    @DisplayName("TC02: Test approval with non-existent user")
    @Rollback(true)
    public void testAutoConfirmRegisters_UserNotFound() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiYtaRestController.autoConfirmRegisters(confirmRegisterDto);

        // Assert
        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
        assert response.getBody().toString().contains("Người dùng không tồn tại");
    }

    /**
     * TC03: Test approval with invalid status
     *
     * Input: confirmRegisterDto with non-existent status
     * Expected: Returns HttpStatus.NOT_FOUND with error message
     */
    @Test
    @DisplayName("TC03: Test approval with invalid status")
    @Rollback(true)
    public void testAutoConfirmRegisters_InvalidStatus() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(statusIsApprovedService.findByStatus(anyString())).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiYtaRestController.autoConfirmRegisters(confirmRegisterDto);

        // Assert
        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
        assert response.getBody().toString().contains("Trạng thái hoặc ngày này chưa có đơn đăng kí khám");
    }

    /**
     * TC04: Test approval with no registration forms to approve
     *
     * Input: confirmRegisterDto with valid status but no forms to approve
     * Expected: Returns HttpStatus.NOT_FOUND with error message
     */
    @Test
    @DisplayName("TC04: Test approval with no registration forms to approve")
    @Rollback(true)
    public void testAutoConfirmRegisters_NoFormsToApprove() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(statusIsApprovedService.findByStatus(anyString())).thenReturn(approvedStatus);

        // Mock Calendar setup
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(confirmRegisterDto.getRegisterDate());

        when(scheduleService.findByDayMonthYear(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH))).thenReturn(testSchedule);

        when(statusIsApprovedService.findByStatus("CHECKING")).thenReturn(checkingStatus);
        when(medicalRegistryListService.findByScheduleAndStatusIsApproved2(any(Schedule.class), any(StatusIsApproved.class)))
                .thenReturn(new ArrayList<>()); // Empty list

        // Act
        ResponseEntity<Object> response = apiYtaRestController.autoConfirmRegisters(confirmRegisterDto);

        // Assert
        assert response.getStatusCode() == HttpStatus.NOT_FOUND;
        assert response.getBody().toString().contains("Không tồn tại đơn đăng kí để xét duyệt vào ngày này");
    }

    /**
     * TC05: Test approval with specific emails only
     *
     * Input: confirmRegisterDto with specific emails to approve
     * Expected: Only registrations matching emails are approved
     */
    @Test
    @DisplayName("TC05: Test approval with specific emails only")
    @Rollback(true)
    public void testAutoConfirmRegisters_SpecificEmails() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(statusIsApprovedService.findByStatus(anyString())).thenReturn(approvedStatus);

        // Mock Calendar setup
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(confirmRegisterDto.getRegisterDate());

        when(scheduleService.findByDayMonthYear(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH))).thenReturn(testSchedule);

        when(statusIsApprovedService.findByStatus("CHECKING")).thenReturn(checkingStatus);

        // Create a different user for the second MRL
        User user2 = new User();
        user2.setId(2);
        user2.setEmail("another@example.com");

        // Create a new list with only one matching email
        List<MedicalRegistryList> filteredList = new ArrayList<>();
        MedicalRegistryList mrl1 = new MedicalRegistryList();
        mrl1.setId(1);
        mrl1.setName("Patient 1");
        mrl1.setStatusIsApproved(checkingStatus);
        mrl1.setSchedule(testSchedule);
        mrl1.setUser(currentUser); // This one has test@example.com

        MedicalRegistryList mrl2 = new MedicalRegistryList();
        mrl2.setId(2);
        mrl2.setName("Patient 2");
        mrl2.setStatusIsApproved(checkingStatus);
        mrl2.setSchedule(testSchedule);
        mrl2.setUser(user2); // This one has another@example.com

        filteredList.add(mrl1);
        filteredList.add(mrl2);

        when(medicalRegistryListService.findByScheduleAndStatusIsApproved2(any(Schedule.class), any(StatusIsApproved.class)))
                .thenReturn(filteredList);

        // Set specific email filter to match only the first MRL
        List<String> specificEmails = Arrays.asList("test@example.com");
        confirmRegisterDto.setEmails(specificEmails);

        // Act
        ResponseEntity<Object> response = apiYtaRestController.autoConfirmRegisters(confirmRegisterDto);

        // Assert
        assert response.getStatusCode() == HttpStatus.OK;
        // Should save only for matching emails (just one)
        verify(medicalRegistryListService, times(1)).saveMedicalRegistryList(any(MedicalRegistryList.class));
    }
}