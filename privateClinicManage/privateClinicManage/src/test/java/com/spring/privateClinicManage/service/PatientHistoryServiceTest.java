package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.spring.privateClinicManage.dto.MrlAndMeHistoryDto;
import com.spring.privateClinicManage.dto.PaymentHistoryDto;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.MedicalRegistryListRepository;
import com.spring.privateClinicManage.service.impl.StatsServiceImpl;

/**
 * Unit tests for patient history related services
 * These tests verify that the service layer correctly processes and returns
 * patient history data from the repository layer.
 */
@ExtendWith(MockitoExtension.class)
public class PatientHistoryServiceTest {

    @Mock
    private MedicalRegistryListRepository medicalRegistryListRepository;

    @InjectMocks
    private StatsServiceImpl statsService;

    private User testUser;
    private List<MrlAndMeHistoryDto> mrlHistoryData;
    private List<PaymentHistoryDto> paymentPhase1Data;
    private List<PaymentHistoryDto> paymentPhase2Data;

    @BeforeEach
    public void setup() {
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
    }

    /**
     * Test case: TC_PATIENT_HISTORY_01
     * Test paginated statistics for user medical history
     * Input: Page number, page size, and user
     * Expected output: Paginated list of MrlAndMeHistoryDto
     *
     * Mục tiêu: Kiểm tra phân trang thống kê lịch sử khám bệnh của người dùng
     * Đầu vào: Số trang, kích thước trang và thông tin người dùng
     * Đầu ra mong đợi: Danh sách phân trang lịch sử khám bệnh
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_01: Test paginated statistics for user medical history")
    @Rollback(true)
    public void testPaginatedStatsUserMrlAndMeHistory() {
        // Arrange
        Integer page = 1;
        Integer size = 2;

        // Mock repository response
        when(medicalRegistryListRepository.statsUserMrlAndMeHistory(any(User.class))).thenReturn(mrlHistoryData);

        // Act
        Page<MrlAndMeHistoryDto> result = statsService.paginatedStatsUserMrlAndMeHistory(page, size, testUser);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size()); // Should return 2 items as per page size
        assertEquals(3, result.getTotalElements()); // Total elements should be 3
        assertEquals("Patient Record 1", result.getContent().get(0).getName());
        assertEquals(3L, result.getContent().get(0).getTotal());

        // Verify repository was called
        verify(medicalRegistryListRepository, times(1)).statsUserMrlAndMeHistory(testUser);
    }

    /**
     * Test case: TC_PATIENT_HISTORY_02
     * Test statistics for payment phase 1 history
     * Input: Patient name
     * Expected output: List of payment phase 1 history for the patient
     *
     * Mục tiêu: Kiểm tra thống kê lịch sử thanh toán giai đoạn 1
     * Đầu vào: Tên bệnh nhân
     * Đầu ra mong đợi: Danh sách lịch sử thanh toán giai đoạn 1 của bệnh nhân
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_02: Test statistics for payment phase 1 history")
    @Rollback(true)
    public void testStatsPaymentPhase1History() {
        // Arrange
        String patientName = "Patient Record 1";

        // Mock repository response
        when(medicalRegistryListRepository.statsPaymentPhase1History(anyString())).thenReturn(paymentPhase1Data);

        // Act
        List<PaymentHistoryDto> result = statsService.statsPaymentPhase1History(patientName);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ORD001", result.get(0).getOrderId());
        assertEquals(100000L, result.get(0).getAmount());
        assertEquals("MOMO", result.get(0).getPartnerCode());

        // Verify repository was called
        verify(medicalRegistryListRepository, times(1)).statsPaymentPhase1History(patientName);
    }

    /**
     * Test case: TC_PATIENT_HISTORY_03
     * Test statistics for payment phase 2 history
     * Input: Patient name
     * Expected output: List of payment phase 2 history for the patient
     *
     * Mục tiêu: Kiểm tra thống kê lịch sử thanh toán giai đoạn 2
     * Đầu vào: Tên bệnh nhân
     * Đầu ra mong đợi: Danh sách lịch sử thanh toán giai đoạn 2 của bệnh nhân
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_03: Test statistics for payment phase 2 history")
    @Rollback(true)
    public void testStatsPaymentPhase2History() {
        // Arrange
        String patientName = "Patient Record 1";

        // Mock repository response
        when(medicalRegistryListRepository.statsPaymentPhase2History(anyString())).thenReturn(paymentPhase2Data);

        // Act
        List<PaymentHistoryDto> result = statsService.statsPaymentPhase2History(patientName);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ORD003", result.get(0).getOrderId());
        assertEquals(200000L, result.get(0).getAmount());
        assertEquals("MOMO", result.get(0).getPartnerCode());

        // Verify repository was called
        verify(medicalRegistryListRepository, times(1)).statsPaymentPhase2History(patientName);
    }

    /**
     * Test case: TC_PATIENT_HISTORY_04
     * Test sorting payment history by created date
     * Input: Combined list of payment history DTOs
     * Expected output: Sorted list of payment history DTOs by created date (descending)
     *
     * Mục tiêu: Kiểm tra sắp xếp lịch sử thanh toán theo ngày tạo
     * Đầu vào: Danh sách kết hợp các DTO lịch sử thanh toán
     * Đầu ra mong đợi: Danh sách lịch sử thanh toán đã sắp xếp theo ngày tạo (giảm dần)
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_04: Test sorting payment history by created date")
    @Rollback(true)
    public void testSortByCreatedDate() {
        // Arrange
        List<PaymentHistoryDto> combinedPaymentHistory = new ArrayList<>();
        combinedPaymentHistory.addAll(paymentPhase1Data);
        combinedPaymentHistory.addAll(paymentPhase2Data);

        // Act
        List<PaymentHistoryDto> result = statsService.sortByCreatedDate(combinedPaymentHistory);

        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());

        // Verify the sorting order (newest first)
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result.get(0).getCreatedDate());
        int firstMonth = calendar.get(Calendar.MONTH);

        calendar.setTime(result.get(1).getCreatedDate());
        int secondMonth = calendar.get(Calendar.MONTH);

        calendar.setTime(result.get(2).getCreatedDate());
        int thirdMonth = calendar.get(Calendar.MONTH);

        calendar.setTime(result.get(3).getCreatedDate());
        int fourthMonth = calendar.get(Calendar.MONTH);

        // Check that dates are in descending order
        assertTrue(firstMonth >= secondMonth);
        assertTrue(secondMonth >= thirdMonth);
        assertTrue(thirdMonth >= fourthMonth);
    }

    /**
     * Test case: TC_PATIENT_HISTORY_05
     * Test paginated statistics with empty result
     * Input: Page number, page size, and user with no history
     * Expected output: Empty paginated list
     *
     * Mục tiêu: Kiểm tra phân trang thống kê với kết quả rỗng
     * Đầu vào: Số trang, kích thước trang và thông tin người dùng không có lịch sử
     * Đầu ra mong đợi: Danh sách phân trang rỗng
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_05: Test paginated statistics with empty result")
    @Rollback(true)
    public void testPaginatedStatsUserMrlAndMeHistoryEmpty() {
        // Arrange
        Integer page = 1;
        Integer size = 10;
        List<MrlAndMeHistoryDto> emptyList = new ArrayList<>();

        // Mock repository response
        when(medicalRegistryListRepository.statsUserMrlAndMeHistory(any(User.class))).thenReturn(emptyList);

        // Act
        Page<MrlAndMeHistoryDto> result = statsService.paginatedStatsUserMrlAndMeHistory(page, size, testUser);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getTotalElements());

        // Verify repository was called
        verify(medicalRegistryListRepository, times(1)).statsUserMrlAndMeHistory(testUser);
    }
}
