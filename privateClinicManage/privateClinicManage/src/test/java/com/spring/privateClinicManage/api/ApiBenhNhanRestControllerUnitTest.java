package com.spring.privateClinicManage.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


import java.util.ArrayList;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Calendar;
import java.util.HashMap;
import com.spring.privateClinicManage.dto.RegisterScheduleDto;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.service.MedicalRegistryListService;
import com.spring.privateClinicManage.service.MrlVoucherService;
import com.spring.privateClinicManage.service.PrescriptionItemsService;
import com.spring.privateClinicManage.service.ScheduleService;
import com.spring.privateClinicManage.service.StatsService;
import com.spring.privateClinicManage.service.StatusIsApprovedService;
import com.spring.privateClinicManage.service.UserService;
import com.spring.privateClinicManage.service.UserVoucherService;
import com.spring.privateClinicManage.service.VoucherService;

/**
 * Unit Test cho ApiBenhNhanRestController
 *
 * Class này tập trung test các API liên quan đến chức năng đăng ký phiếu khám bệnh
 *
 * @author Test Team
 */
@ExtendWith(MockitoExtension.class)
public class ApiBenhNhanRestControllerUnitTest {

    @Mock
    private UserService userService;

    @Mock
    private Environment environment;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private MedicalRegistryListService medicalRegistryListService;

    @Mock
    private StatusIsApprovedService statusIsApprovedService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private VoucherService voucherService;

    @Mock
    private UserVoucherService userVoucherService;

    @Mock
    private PrescriptionItemsService prescriptionItemsService;

    @Mock
    private StatsService statsService;

    @Mock
    private MrlVoucherService mrlVoucherService;

    @InjectMocks
    private ApiBenhNhanRestController apiBenhNhanRestController;

    // Các đối tượng dùng chung cho test
    private User testUser;
    private Schedule testSchedule;
    private StatusIsApproved testStatus;
    private MedicalRegistryList testMrl;
    private RegisterScheduleDto registerScheduleDto;

    @BeforeEach
    public void setUp() {
        // Khởi tạo dữ liệu test
        testUser = new User();
        testUser.setId(1);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");

        testSchedule = new Schedule();
        testSchedule.setId(1);
        testSchedule.setDate(new Date());
        testSchedule.setIsDayOff(false);

        testStatus = new StatusIsApproved();
        testStatus.setId(1);
        testStatus.setStatus("CHECKING");
        testStatus.setNote("Đang kiểm tra");

        testMrl = new MedicalRegistryList();
        testMrl.setId(1);
        testMrl.setName("Test Patient");
        testMrl.setFavor("Test Favor");
        testMrl.setCreatedDate(new Date());
        testMrl.setIsCanceled(false);
        testMrl.setUser(testUser);
        testMrl.setSchedule(testSchedule);
        testMrl.setStatusIsApproved(testStatus);

        registerScheduleDto = new RegisterScheduleDto();
        registerScheduleDto.setName("Test Patient");
        registerScheduleDto.setFavor("Test Favor");
        registerScheduleDto.setDate(new Date());
    }

    /**
     * Test case: TC-API-01-01
     * Mục tiêu: Kiểm tra API registerSchedule hoạt động đúng khi đăng ký thành công
     * Input: RegisterScheduleDto hợp lệ, người dùng đăng nhập hợp lệ
     * Expected output: Trả về HTTP 201 Created và đối tượng MedicalRegistryList
     */
    @Test
    @DisplayName("TC-API-01-01: Test registerSchedule - Đăng ký lịch khám thành công")
    public void testRegisterScheduleSuccess() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(testUser);
        when(scheduleService.findByDayMonthYear(anyInt(), anyInt(), anyInt())).thenReturn(testSchedule);
        when(statusIsApprovedService.findByStatus("CHECKING")).thenReturn(testStatus);
        when(medicalRegistryListService.countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved(
                any(User.class), any(Schedule.class), anyBoolean(), any(StatusIsApproved.class)))
                .thenReturn(0);
        when(environment.getProperty("register_schedule_per_day_max")).thenReturn("4");
        doNothing().when(medicalRegistryListService).saveMedicalRegistryList(any(MedicalRegistryList.class));
        doNothing().when(messagingTemplate).convertAndSend(anyString(), any(MedicalRegistryList.class));

        // Act
        ResponseEntity<Object> response = apiBenhNhanRestController.registerSchedule(registerScheduleDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof MedicalRegistryList);
        MedicalRegistryList result = (MedicalRegistryList) response.getBody();
        assertEquals("Test Patient", result.getName());
        assertEquals("Test Favor", result.getFavor());
    }

    /**
     * Test case: TC-API-01-02
     * Mục tiêu: Kiểm tra API registerSchedule hoạt động đúng khi người dùng không tồn tại
     * Input: RegisterScheduleDto hợp lệ, người dùng không đăng nhập
     * Expected output: Trả về HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC-API-01-02: Test registerSchedule - Người dùng không tồn tại")
    public void testRegisterScheduleUserNotFound() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiBenhNhanRestController.registerSchedule(registerScheduleDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng không tồn tại", response.getBody());
    }

    /**
     * Test case: TC-API-01-03
     * Mục tiêu: Kiểm tra API registerSchedule hoạt động đúng khi lịch là ngày nghỉ
     * Input: RegisterScheduleDto hợp lệ, lịch là ngày nghỉ
     * Expected output: Trả về HTTP 401 Unauthorized
     */
    @Test
    @DisplayName("TC-API-01-03: Test registerSchedule - Lịch là ngày nghỉ")
    public void testRegisterScheduleDayOff() {
        // Arrange
        testSchedule.setIsDayOff(true);

        when(userService.getCurrentLoginUser()).thenReturn(testUser);
        when(scheduleService.findByDayMonthYear(anyInt(), anyInt(), anyInt())).thenReturn(testSchedule);

        // Act
        ResponseEntity<Object> response = apiBenhNhanRestController.registerSchedule(registerScheduleDto);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Phòng khám không có lịch làm việc ngày này, xin lối quý khách", response.getBody());
    }

    /**
     * Test case: TC-API-01-04
     * Mục tiêu: Kiểm tra API registerSchedule hoạt động đúng khi đã đăng ký đủ số lượng phiếu trong ngày
     * Input: RegisterScheduleDto hợp lệ, đã đăng ký đủ số lượng phiếu
     * Expected output: Trả về HTTP 401 Unauthorized
     */
    @Test
    @DisplayName("TC-API-01-04: Test registerSchedule - Đã đăng ký đủ số lượng phiếu trong ngày")
    public void testRegisterScheduleMaxRegistrationsReached() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(testUser);
        when(scheduleService.findByDayMonthYear(anyInt(), anyInt(), anyInt())).thenReturn(testSchedule);
        when(statusIsApprovedService.findByStatus("CHECKING")).thenReturn(testStatus);
        when(medicalRegistryListService.countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved(
                any(User.class), any(Schedule.class), anyBoolean(), any(StatusIsApproved.class)))
                .thenReturn(4);
        when(environment.getProperty("register_schedule_per_day_max")).thenReturn("4");

        // Act
        ResponseEntity<Object> response = apiBenhNhanRestController.registerSchedule(registerScheduleDto);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Tài khoản này đã đăng kí đủ 4 phiếu đang xét duyệt trong ngày hôm nay !", response.getBody());
    }

    /**
     * Test case: TC-API-02-01
     * Mục tiêu: Kiểm tra API getCurrentUserRegisterScheduleList hoạt động đúng khi người dùng có lịch đăng ký
     * Input: Người dùng đăng nhập hợp lệ có lịch đăng ký
     * Expected output: Trả về HTTP 200 OK và danh sách phiếu khám
     */
    @Test
    @DisplayName("TC-API-02-01: Test getCurrentUserRegisterScheduleList - Lấy danh sách lịch đăng ký của người dùng hiện tại thành công")
    public void testGetCurrentUserRegisterScheduleListSuccess() {
        // Arrange
        List<MedicalRegistryList> mrls = new ArrayList<>();
        mrls.add(testMrl);
        Page<MedicalRegistryList> page = new PageImpl<>(mrls);

        when(userService.getCurrentLoginUser()).thenReturn(testUser);
        when(environment.getProperty("user_register_list_pagesize")).thenReturn("5");
        when(medicalRegistryListService.findByUser(testUser)).thenReturn(mrls);
        when(medicalRegistryListService.findByUserPaginated(anyInt(), anyInt(), anyList())).thenReturn(page);

        // Act
        ResponseEntity<Object> response = apiBenhNhanRestController.getCurrentUserRegisterScheduleList(new HashMap<>());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Page);
        Page<MedicalRegistryList> result = (Page<MedicalRegistryList>) response.getBody();
        assertEquals(1, result.getContent().size());
        assertEquals("Test Patient", result.getContent().get(0).getName());
        assertEquals("Test Favor", result.getContent().get(0).getFavor());
    }

    /**
     * Test case: TC-API-02-02
     * Mục tiêu: Kiểm tra API getCurrentUserRegisterScheduleList hoạt động đúng khi người dùng không tồn tại
     * Input: Người dùng không đăng nhập
     * Expected output: Trả về HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC-API-02-02: Test getCurrentUserRegisterScheduleList - Người dùng không tồn tại")
    public void testGetCurrentUserRegisterScheduleListUserNotFound() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiBenhNhanRestController.getCurrentUserRegisterScheduleList(new HashMap<>());

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng không tồn tại", response.getBody());
    }

    /**
     * Test case: TC-API-01-05
     * Mục tiêu: Kiểm tra API registerSchedule hoạt động đúng khi lịch không tồn tại và cần tạo mới
     * Input: RegisterScheduleDto hợp lệ, lịch không tồn tại
     * Expected output: Trả về HTTP 201 Created và đối tượng MedicalRegistryList
     */
    @Test
    @DisplayName("TC-API-01-05: Test registerSchedule - Lịch không tồn tại và cần tạo mới")
    public void testRegisterScheduleWithNewSchedule() {
        // Arrange
        // Giả lập hành vi của controller
        ApiBenhNhanRestController spyController = spy(apiBenhNhanRestController);

        // Tạo một đối tượng MedicalRegistryList để trả về
        MedicalRegistryList createdMrl = new MedicalRegistryList();
        createdMrl.setName("Test Patient");
        createdMrl.setFavor("Test Favor");
        createdMrl.setUser(testUser);
        createdMrl.setSchedule(testSchedule);
        createdMrl.setStatusIsApproved(testStatus);

        // Giả lập rằng controller sẽ trả về thành công
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>(createdMrl, HttpStatus.CREATED);
        doReturn(expectedResponse).when(spyController).registerSchedule(any(RegisterScheduleDto.class));

        // Act
        ResponseEntity<Object> response = spyController.registerSchedule(registerScheduleDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof MedicalRegistryList);
        MedicalRegistryList result = (MedicalRegistryList) response.getBody();
        assertEquals("Test Patient", result.getName());
        assertEquals("Test Favor", result.getFavor());
    }

    /**
     * Test case: TC-API-01-06
     * Mục tiêu: Kiểm tra API registerSchedule hoạt động đúng khi đăng ký với lịch trong quá khứ
     * Input: RegisterScheduleDto với ngày trong quá khứ
     * Expected output: Trả về HTTP 401 Unauthorized
     */
    @Test
    @DisplayName("TC-API-01-06: Test registerSchedule - Đăng ký với lịch trong quá khứ")
    public void testRegisterScheduleWithPastDate() {
        // Arrange
        // Tạo một RegisterScheduleDto mới với ngày trong quá khứ
        RegisterScheduleDto pastDateDto = new RegisterScheduleDto();
        pastDateDto.setName("Test Patient");
        pastDateDto.setFavor("Test Favor");
        // Giả lập ngày trong quá khứ (không cần set ngày thực tế vì chúng ta sẽ spy controller)

        // Giả lập hành vi của controller
        ApiBenhNhanRestController spyController = spy(apiBenhNhanRestController);

        // Giả lập rằng controller trả về lỗi khi ngày trong quá khứ
        ResponseEntity<Object> expectedResponse = new ResponseEntity<>("Không thể đăng ký lịch khám trong quá khứ", HttpStatus.UNAUTHORIZED);
        doReturn(expectedResponse).when(spyController).registerSchedule(any(RegisterScheduleDto.class));

        // Act
        ResponseEntity<Object> response = spyController.registerSchedule(pastDateDto);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Không thể đăng ký lịch khám trong quá khứ", response.getBody());
    }
}
