package com.spring.privateClinicManage.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.privateClinicManage.api.ApiBenhNhanRestController;
import com.spring.privateClinicManage.api.ApiAnyRoleRestController;
import com.spring.privateClinicManage.dto.HisotryUserMedicalRegisterDto;
import com.spring.privateClinicManage.dto.MrlAndMeHistoryDto;
import com.spring.privateClinicManage.dto.NameDto;
import com.spring.privateClinicManage.dto.PaymentHistoryDto;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.service.MedicalRegistryListService;
import com.spring.privateClinicManage.service.StatsService;
import com.spring.privateClinicManage.service.UserService;

/**
 * Unit tests for patient history related controllers
 * These tests verify that the controller layer correctly handles requests for
 * patient history data and returns appropriate responses.
 */
@ExtendWith(MockitoExtension.class)
public class PatientHistoryControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private StatsService statsService;

    @Mock
    private MedicalRegistryListService medicalRegistryListService;

    @InjectMocks
    private ApiBenhNhanRestController apiBenhNhanRestController;

    @InjectMocks
    private ApiAnyRoleRestController apiAnyRoleRestController;

    private MockMvc mockMvcBenhNhan;
    private MockMvc mockMvcAnyRole;
    private ObjectMapper objectMapper;
    private User testUser;
    private List<MrlAndMeHistoryDto> mrlHistoryData;
    private List<PaymentHistoryDto> paymentPhase1Data;
    private List<PaymentHistoryDto> paymentPhase2Data;
    private List<MedicalRegistryList> medicalRegistryLists;

    @BeforeEach
    public void setup() {
        // Initialize MockMvc
        mockMvcBenhNhan = MockMvcBuilders.standaloneSetup(apiBenhNhanRestController).build();
        mockMvcAnyRole = MockMvcBuilders.standaloneSetup(apiAnyRoleRestController).build();
        objectMapper = new ObjectMapper();

        // Initialize test user
        testUser = new User();
        testUser.setId(1);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");

        // Initialize MRL history test data
        mrlHistoryData = new ArrayList<>();
        mrlHistoryData.add(new MrlAndMeHistoryDto("Patient Record 1", new Date(), 3L));
        mrlHistoryData.add(new MrlAndMeHistoryDto("Patient Record 2", new Date(), 1L));
        mrlHistoryData.add(new MrlAndMeHistoryDto("Patient Record 3", new Date(), 2L));

        // Initialize payment history test data
        Calendar calendar = Calendar.getInstance();

        paymentPhase1Data = new ArrayList<>();
        calendar.set(2023, 0, 15); // January 15, 2023
        paymentPhase1Data.add(new PaymentHistoryDto("ORD001", calendar.getTime(), "Patient Record 1", 100000L, "Phase 1 payment", "00", "MOMO"));

        calendar.set(2023, 1, 20); // February 20, 2023
        paymentPhase1Data.add(new PaymentHistoryDto("ORD002", calendar.getTime(), "Patient Record 1", 150000L, "Phase 1 payment", "00", "VNPAY"));

        paymentPhase2Data = new ArrayList<>();
        calendar.set(2023, 2, 10); // March 10, 2023
        paymentPhase2Data.add(new PaymentHistoryDto("ORD003", calendar.getTime(), "Patient Record 1", 200000L, "Phase 2 payment", "00", "MOMO"));

        calendar.set(2023, 3, 5); // April 5, 2023
        paymentPhase2Data.add(new PaymentHistoryDto("ORD004", calendar.getTime(), "Patient Record 1", 250000L, "Phase 2 payment", "00", "VNPAY"));

        // Initialize medical registry list data
        medicalRegistryLists = new ArrayList<>();
        MedicalRegistryList mrl1 = new MedicalRegistryList();
        mrl1.setId(1);
        mrl1.setName("Patient Record 1");
        mrl1.setUser(testUser);

        MedicalRegistryList mrl2 = new MedicalRegistryList();
        mrl2.setId(2);
        mrl2.setName("Patient Record 2");
        mrl2.setUser(testUser);

        medicalRegistryLists.add(mrl1);
        medicalRegistryLists.add(mrl2);
    }

    /**
     * Test case: TC_PATIENT_HISTORY_CONTROLLER_01
     * Test getting paginated medical history for the current user
     * Input: Page and size parameters
     * Expected output: Paginated list of MrlAndMeHistoryDto with HTTP 200 OK
     *
     * Mục tiêu: Kiểm tra API lấy lịch sử khám bệnh của người dùng hiện tại
     * Đầu vào: Tham số trang và kích thước trang
     * Đầu ra mong đợi: Danh sách phân trang lịch sử khám bệnh với HTTP 200 OK
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_CONTROLLER_01: Test getting paginated medical history for current user")
    @Rollback(true)
    public void testGetMrlAndMeUserHistory() throws Exception {
        // Arrange
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("size", "3");

        Page<MrlAndMeHistoryDto> pagedResponse = new PageImpl<>(mrlHistoryData);

        when(userService.getCurrentLoginUser()).thenReturn(testUser);
        when(statsService.paginatedStatsUserMrlAndMeHistory(anyInt(), anyInt(), any(User.class)))
            .thenReturn(pagedResponse);

        // Act
        ResponseEntity<Object> response = apiBenhNhanRestController.getMrlAndMeUserHistory(params);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pagedResponse, response.getBody());

        // Verify service methods were called
        verify(userService, times(1)).getCurrentLoginUser();
        verify(statsService, times(1)).paginatedStatsUserMrlAndMeHistory(anyInt(), anyInt(), any(User.class));

        // We'll skip the MockMvc test for this method as it requires authentication
        // and focus on testing the controller method directly
    }

    /**
     * Test case: TC_PATIENT_HISTORY_CONTROLLER_02
     * Test getting medical history when user is not found
     * Input: Page and size parameters with no authenticated user
     * Expected output: Error message with HTTP 404 Not Found
     *
     * Mục tiêu: Kiểm tra API lấy lịch sử khám bệnh khi không tìm thấy người dùng
     * Đầu vào: Tham số trang và kích thước trang, không có người dùng đăng nhập
     * Đầu ra mong đợi: Thông báo lỗi với HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_CONTROLLER_02: Test getting medical history when user is not found")
    @Rollback(true)
    public void testGetMrlAndMeUserHistoryUserNotFound() throws Exception {
        // Arrange
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("size", "3");

        when(userService.getCurrentLoginUser()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiBenhNhanRestController.getMrlAndMeUserHistory(params);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng không tồn tại", response.getBody());

        // Verify service method was called
        verify(userService, times(1)).getCurrentLoginUser();

        // We'll skip the MockMvc test for this method as it requires authentication
        // and focus on testing the controller method directly
    }

    /**
     * Test case: TC_PATIENT_HISTORY_CONTROLLER_03
     * Test getting payment history by patient name
     * Input: NameDto with valid patient name
     * Expected output: Combined list of payment history with HTTP 200 OK
     *
     * Mục tiêu: Kiểm tra API lấy lịch sử thanh toán theo tên bệnh nhân
     * Đầu vào: NameDto với tên bệnh nhân hợp lệ
     * Đầu ra mong đợi: Danh sách kết hợp lịch sử thanh toán với HTTP 200 OK
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_CONTROLLER_03: Test getting payment history by patient name")
    @Rollback(true)
    public void testGetPaymentHistoryByName() throws Exception {
        // Arrange
        NameDto nameDto = new NameDto();
        nameDto.setName("Patient Record 1");

        List<PaymentHistoryDto> combinedPaymentHistory = new ArrayList<>();
        combinedPaymentHistory.addAll(paymentPhase1Data);
        combinedPaymentHistory.addAll(paymentPhase2Data);

        when(userService.getCurrentLoginUser()).thenReturn(testUser);
        when(statsService.statsPaymentPhase1History(anyString())).thenReturn(paymentPhase1Data);
        when(statsService.statsPaymentPhase2History(anyString())).thenReturn(paymentPhase2Data);
        when(statsService.sortByCreatedDate(any())).thenReturn(combinedPaymentHistory);

        // Act
        ResponseEntity<Object> response = apiBenhNhanRestController.getPaymentHistoryByName(nameDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verify service methods were called
        verify(userService, times(1)).getCurrentLoginUser();
        verify(statsService, times(1)).statsPaymentPhase1History(nameDto.getName());
        verify(statsService, times(1)).statsPaymentPhase2History(nameDto.getName());
        verify(statsService, times(1)).sortByCreatedDate(any());

        // We'll skip the MockMvc test for this method as it requires authentication
        // and focus on testing the controller method directly
    }

    /**
     * Test case: TC_PATIENT_HISTORY_CONTROLLER_04
     * Test getting payment history when user is not found
     * Input: NameDto with valid patient name, no authenticated user
     * Expected output: Error message with HTTP 404 Not Found
     *
     * Mục tiêu: Kiểm tra API lấy lịch sử thanh toán khi không tìm thấy người dùng
     * Đầu vào: NameDto với tên bệnh nhân hợp lệ, không có người dùng đăng nhập
     * Đầu ra mong đợi: Thông báo lỗi với HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_CONTROLLER_04: Test getting payment history when user is not found")
    @Rollback(true)
    public void testGetPaymentHistoryByNameUserNotFound() throws Exception {
        // Arrange
        NameDto nameDto = new NameDto();
        nameDto.setName("Patient Record 1");

        when(userService.getCurrentLoginUser()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiBenhNhanRestController.getPaymentHistoryByName(nameDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng không tồn tại", response.getBody());

        // Verify service method was called
        verify(userService, times(1)).getCurrentLoginUser();

        // Verify API endpoint works with MockMvc
        mockMvcBenhNhan.perform(post("/api/benhnhan/get-payment-history-by-name/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nameDto)))
                .andExpect(status().isNotFound());
    }

    /**
     * Test case: TC_PATIENT_HISTORY_CONTROLLER_05
     * Test getting payment history with null patient name
     * Input: NameDto with null patient name
     * Expected output: Error message with HTTP 404 Not Found
     *
     * Mục tiêu: Kiểm tra API lấy lịch sử thanh toán với tên bệnh nhân null
     * Đầu vào: NameDto với tên bệnh nhân null
     * Đầu ra mong đợi: Thông báo lỗi với HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_CONTROLLER_05: Test getting payment history with null patient name")
    @Rollback(true)
    public void testGetPaymentHistoryByNameWithNullName() throws Exception {
        // Arrange
        NameDto nameDto = new NameDto();
        nameDto.setName(null);

        when(userService.getCurrentLoginUser()).thenReturn(testUser);

        // Act
        ResponseEntity<Object> response = apiBenhNhanRestController.getPaymentHistoryByName(nameDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Tên không được rỗng !", response.getBody());

        // Verify service method was called
        verify(userService, times(1)).getCurrentLoginUser();

        // We'll skip the MockMvc test for this method as it requires authentication
        // and focus on testing the controller method directly
    }

    /**
     * Test case: TC_PATIENT_HISTORY_CONTROLLER_06
     * Test getting history of user medical registrations
     * Input: HisotryUserMedicalRegisterDto with valid email and name
     * Expected output: List of MedicalRegistryList with HTTP 200 OK
     *
     * Mục tiêu: Kiểm tra API lấy lịch sử đăng ký khám bệnh của người dùng
     * Đầu vào: HisotryUserMedicalRegisterDto với email và tên hợp lệ
     * Đầu ra mong đợi: Danh sách MedicalRegistryList với HTTP 200 OK
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_CONTROLLER_06: Test getting history of user medical registrations")
    @Rollback(true)
    public void testGetHistoryUserRegister() throws Exception {
        // Arrange
        HisotryUserMedicalRegisterDto dto = new HisotryUserMedicalRegisterDto();
        dto.setEmail("test@example.com");
        dto.setNameRegister("Patient Record 1");

        when(userService.getCurrentLoginUser()).thenReturn(testUser);
        when(userService.findByEmail(anyString())).thenReturn(testUser);
        when(medicalRegistryListService.findAllMrlByUserAndName(any(User.class), anyString()))
            .thenReturn(medicalRegistryLists);

        // Act
        ResponseEntity<Object> response = apiAnyRoleRestController.getHistoryUserRegister(dto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        // Verify service methods were called
        verify(userService, times(1)).getCurrentLoginUser();
        verify(userService, times(1)).findByEmail(dto.getEmail());
        verify(medicalRegistryListService, times(1)).findAllMrlByUserAndName(testUser, dto.getNameRegister());

        // We'll skip the MockMvc test for this method as it requires authentication
        // and focus on testing the controller method directly
    }

    /**
     * Test case: TC_PATIENT_HISTORY_CONTROLLER_07
     * Test getting history when current user is not found
     * Input: HisotryUserMedicalRegisterDto with valid email and name
     * Expected output: Error message with HTTP 404 Not Found
     *
     * Mục tiêu: Kiểm tra API lấy lịch sử khi không tìm thấy người dùng hiện tại
     * Đầu vào: HisotryUserMedicalRegisterDto với email và tên hợp lệ
     * Đầu ra mong đợi: Thông báo lỗi với HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_CONTROLLER_07: Test getting history when current user is not found")
    @Rollback(true)
    public void testGetHistoryUserRegisterCurrentUserNotFound() throws Exception {
        // Arrange
        HisotryUserMedicalRegisterDto dto = new HisotryUserMedicalRegisterDto();
        dto.setEmail("test@example.com");
        dto.setNameRegister("Patient Record 1");

        when(userService.getCurrentLoginUser()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiAnyRoleRestController.getHistoryUserRegister(dto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng không tồn tại", response.getBody());

        // Verify service method was called
        verify(userService, times(1)).getCurrentLoginUser();

        // Verify API endpoint works with MockMvc
        mockMvcAnyRole.perform(post("/api/get-history-user-register/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    /**
     * Test case: TC_PATIENT_HISTORY_CONTROLLER_08
     * Test getting history when patient is not found
     * Input: HisotryUserMedicalRegisterDto with invalid email
     * Expected output: Error message with HTTP 404 Not Found
     *
     * Mục tiêu: Kiểm tra API lấy lịch sử khi không tìm thấy bệnh nhân
     * Đầu vào: HisotryUserMedicalRegisterDto với email không hợp lệ
     * Đầu ra mong đợi: Thông báo lỗi với HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_CONTROLLER_08: Test getting history when patient is not found")
    @Rollback(true)
    public void testGetHistoryUserRegisterPatientNotFound() throws Exception {
        // Arrange
        HisotryUserMedicalRegisterDto dto = new HisotryUserMedicalRegisterDto();
        dto.setEmail("nonexistent@test.com");
        dto.setNameRegister("Patient Record 1");

        when(userService.getCurrentLoginUser()).thenReturn(testUser);
        when(userService.findByEmail(anyString())).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiAnyRoleRestController.getHistoryUserRegister(dto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng không tồn tại", response.getBody());

        // Verify service methods were called
        verify(userService, times(1)).getCurrentLoginUser();
        verify(userService, times(1)).findByEmail(dto.getEmail());

        // We'll skip the MockMvc test for this method as it requires authentication
        // and focus on testing the controller method directly
    }
}
