package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.spring.privateClinicManage.dto.MrlAndMeHistoryDto;
import com.spring.privateClinicManage.dto.PaymentHistoryDto;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.MedicalRegistryListRepository;
import com.spring.privateClinicManage.repository.MedicineRepository;
import com.spring.privateClinicManage.repository.PaymentDetailRepository;
import com.spring.privateClinicManage.service.impl.StatsServiceImpl;

/**
 * Unit Test for StatsService
 * 
 * This test class focuses on the system data management functionality,
 * specifically the statistics and reporting features.
 * 
 * The tests verify that the StatsService correctly processes and returns
 * statistical data from various repositories.
 */
@ExtendWith(MockitoExtension.class)
@Transactional
public class StatsServiceTest {

    @Mock
    private MedicineRepository medicineRepository;
    
    @Mock
    private PaymentDetailRepository paymentDetailRepository;
    
    @Mock
    private MedicalRegistryListRepository medicalRegistryListRepository;
    
    @InjectMocks
    private StatsServiceImpl statsService;
    
    // Test data
    private User testUser;
    private List<Object[]> medicineStatsData;
    private List<Object[]> revenueStatsData;
    private List<MrlAndMeHistoryDto> mrlHistoryData;
    private List<PaymentHistoryDto> paymentPhase1Data;
    private List<PaymentHistoryDto> paymentPhase2Data;
    
    @BeforeEach
    public void setUp() {
        // Initialize test user
        testUser = new User();
        testUser.setId(1);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        
        // Initialize medicine statistics test data
        medicineStatsData = new ArrayList<>();
        medicineStatsData.add(new Object[]{"Paracetamol", 500L});
        medicineStatsData.add(new Object[]{"Amoxicillin", 300L});
        
        // Initialize revenue statistics test data
        revenueStatsData = new ArrayList<>();
        revenueStatsData.add(new Object[]{1, 1500000L}); // January
        revenueStatsData.add(new Object[]{2, 2000000L}); // February
        
        // Initialize MRL history test data
        mrlHistoryData = new ArrayList<>();
        mrlHistoryData.add(new MrlAndMeHistoryDto("Patient 1", new Date(), 3L));
        mrlHistoryData.add(new MrlAndMeHistoryDto("Patient 2", new Date(), 1L));
        mrlHistoryData.add(new MrlAndMeHistoryDto("Patient 3", new Date(), 2L));
        
        // Initialize payment history test data
        Calendar calendar = Calendar.getInstance();
        
        paymentPhase1Data = new ArrayList<>();
        calendar.set(2023, 0, 15); // January 15, 2023
        paymentPhase1Data.add(new PaymentHistoryDto("ORD001", calendar.getTime(), "Patient 1", 100000L, "Phase 1 payment", "00", "MOMO"));
        
        calendar.set(2023, 1, 20); // February 20, 2023
        paymentPhase1Data.add(new PaymentHistoryDto("ORD002", calendar.getTime(), "Patient 1", 150000L, "Phase 1 payment", "00", "VNPAY"));
        
        paymentPhase2Data = new ArrayList<>();
        calendar.set(2023, 2, 10); // March 10, 2023
        paymentPhase2Data.add(new PaymentHistoryDto("ORD003", calendar.getTime(), "Patient 1", 200000L, "Phase 2 payment", "00", "MOMO"));
        
        calendar.set(2023, 3, 5); // April 5, 2023
        paymentPhase2Data.add(new PaymentHistoryDto("ORD004", calendar.getTime(), "Patient 1", 250000L, "Phase 2 payment", "00", "CASH"));
    }
    
    /**
     * Test case: TC_STATS_01
     * Test statistics by medicine prognosis
     * Input: Year and month parameters
     * Expected output: List of medicine statistics with name and prognosis sum
     * 
     * Mục tiêu: Kiểm tra thống kê theo dự báo thuốc
     * Đầu vào: Tham số năm và tháng
     * Đầu ra mong đợi: Danh sách thống kê thuốc với tên và tổng dự báo
     */
    @Test
    @DisplayName("TC_STATS_01: Test statistics by medicine prognosis")
    @Rollback(true)
    public void testStatsByPrognosisMedicine() {
        // Arrange
        Integer year = 2023;
        Integer month = 5;
        when(medicineRepository.statsByPrognosisMedicine(year, month)).thenReturn(medicineStatsData);
        
        // Act
        List<Object[]> result = statsService.statsByPrognosisMedicine(year, month);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Paracetamol", result.get(0)[0]);
        assertEquals(500L, result.get(0)[1]);
        assertEquals("Amoxicillin", result.get(1)[0]);
        assertEquals(300L, result.get(1)[1]);
        
        // Verify repository method was called with correct parameters
        verify(medicineRepository, times(1)).statsByPrognosisMedicine(year, month);
    }
    
    /**
     * Test case: TC_STATS_02
     * Test statistics by revenue
     * Input: Year parameter
     * Expected output: List of monthly revenue statistics
     * 
     * Mục tiêu: Kiểm tra thống kê doanh thu
     * Đầu vào: Tham số năm
     * Đầu ra mong đợi: Danh sách thống kê doanh thu theo tháng
     */
    @Test
    @DisplayName("TC_STATS_02: Test statistics by revenue")
    @Rollback(true)
    public void testStatsByRevenue() {
        // Arrange
        Integer year = 2023;
        when(paymentDetailRepository.statsByRevenue(year)).thenReturn(revenueStatsData);
        
        // Act
        List<Object[]> result = statsService.statsByRevenue(year);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0)[0]); // January
        assertEquals(1500000L, result.get(0)[1]); // January revenue
        assertEquals(2, result.get(1)[0]); // February
        assertEquals(2000000L, result.get(1)[1]); // February revenue
        
        // Verify repository method was called with correct parameters
        verify(paymentDetailRepository, times(1)).statsByRevenue(year);
    }
    
    /**
     * Test case: TC_STATS_03
     * Test paginated statistics for user MRL and ME history
     * Input: Page, size, and user parameters
     * Expected output: Paginated list of user medical history
     * 
     * Mục tiêu: Kiểm tra thống kê phân trang lịch sử khám bệnh của người dùng
     * Đầu vào: Tham số trang, kích thước trang và người dùng
     * Đầu ra mong đợi: Danh sách phân trang lịch sử khám bệnh
     */
    @Test
    @DisplayName("TC_STATS_03: Test paginated statistics for user MRL and ME history")
    @Rollback(true)
    public void testPaginatedStatsUserMrlAndMeHistory() {
        // Arrange
        Integer page = 1;
        Integer size = 2;
        when(medicalRegistryListRepository.statsUserMrlAndMeHistory(testUser)).thenReturn(mrlHistoryData);
        
        // Act
        Page<MrlAndMeHistoryDto> result = statsService.paginatedStatsUserMrlAndMeHistory(page, size, testUser);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size()); // Should return 2 items (page size)
        assertEquals(3, result.getTotalElements()); // Total 3 items
        assertEquals(2, result.getTotalPages()); // 3 items with page size 2 = 2 pages
        assertEquals("Patient 1", result.getContent().get(0).getName());
        assertEquals("Patient 2", result.getContent().get(1).getName());
        
        // Verify repository method was called with correct parameters
        verify(medicalRegistryListRepository, times(1)).statsUserMrlAndMeHistory(testUser);
    }
    
    /**
     * Test case: TC_STATS_04
     * Test statistics for payment phase 1 history
     * Input: Patient name
     * Expected output: List of payment phase 1 history for the patient
     * 
     * Mục tiêu: Kiểm tra thống kê lịch sử thanh toán giai đoạn 1
     * Đầu vào: Tên bệnh nhân
     * Đầu ra mong đợi: Danh sách lịch sử thanh toán giai đoạn 1 của bệnh nhân
     */
    @Test
    @DisplayName("TC_STATS_04: Test statistics for payment phase 1 history")
    @Rollback(true)
    public void testStatsPaymentPhase1History() {
        // Arrange
        String patientName = "Patient 1";
        when(medicalRegistryListRepository.statsPaymentPhase1History(patientName)).thenReturn(paymentPhase1Data);
        
        // Act
        List<PaymentHistoryDto> result = statsService.statsPaymentPhase1History(patientName);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ORD001", result.get(0).getOrderId());
        assertEquals("ORD002", result.get(1).getOrderId());
        assertEquals(100000L, result.get(0).getAmount());
        assertEquals(150000L, result.get(1).getAmount());
        
        // Verify repository method was called with correct parameters
        verify(medicalRegistryListRepository, times(1)).statsPaymentPhase1History(patientName);
    }
    
    /**
     * Test case: TC_STATS_05
     * Test statistics for payment phase 2 history
     * Input: Patient name
     * Expected output: List of payment phase 2 history for the patient
     * 
     * Mục tiêu: Kiểm tra thống kê lịch sử thanh toán giai đoạn 2
     * Đầu vào: Tên bệnh nhân
     * Đầu ra mong đợi: Danh sách lịch sử thanh toán giai đoạn 2 của bệnh nhân
     */
    @Test
    @DisplayName("TC_STATS_05: Test statistics for payment phase 2 history")
    @Rollback(true)
    public void testStatsPaymentPhase2History() {
        // Arrange
        String patientName = "Patient 1";
        when(medicalRegistryListRepository.statsPaymentPhase2History(patientName)).thenReturn(paymentPhase2Data);
        
        // Act
        List<PaymentHistoryDto> result = statsService.statsPaymentPhase2History(patientName);
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("ORD003", result.get(0).getOrderId());
        assertEquals("ORD004", result.get(1).getOrderId());
        assertEquals(200000L, result.get(0).getAmount());
        assertEquals(250000L, result.get(1).getAmount());
        
        // Verify repository method was called with correct parameters
        verify(medicalRegistryListRepository, times(1)).statsPaymentPhase2History(patientName);
    }
    
    /**
     * Test case: TC_STATS_06
     * Test sorting payment history by created date
     * Input: List of payment history DTOs
     * Expected output: List sorted by created date in descending order
     * 
     * Mục tiêu: Kiểm tra sắp xếp lịch sử thanh toán theo ngày tạo
     * Đầu vào: Danh sách DTO lịch sử thanh toán
     * Đầu ra mong đợi: Danh sách được sắp xếp theo ngày tạo giảm dần
     */
    @Test
    @DisplayName("TC_STATS_06: Test sorting payment history by created date")
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
        
        // Check if sorted in descending order by created date
        for (int i = 0; i < result.size() - 1; i++) {
            assertTrue(result.get(i).getCreatedDate().compareTo(result.get(i + 1).getCreatedDate()) >= 0);
        }
        
        // The newest date should be first
        assertEquals("ORD004", result.get(0).getOrderId()); // April 5, 2023
        assertEquals("ORD003", result.get(1).getOrderId()); // March 10, 2023
        assertEquals("ORD002", result.get(2).getOrderId()); // February 20, 2023
        assertEquals("ORD001", result.get(3).getOrderId()); // January 15, 2023
    }
    
    /**
     * Test case: TC_STATS_07
     * Test statistics by medicine prognosis with empty result
     * Input: Year and month parameters with no data
     * Expected output: Empty list
     * 
     * Mục tiêu: Kiểm tra thống kê theo dự báo thuốc với kết quả rỗng
     * Đầu vào: Tham số năm và tháng không có dữ liệu
     * Đầu ra mong đợi: Danh sách rỗng
     */
    @Test
    @DisplayName("TC_STATS_07: Test statistics by medicine prognosis with empty result")
    @Rollback(true)
    public void testStatsByPrognosisMedicine_EmptyResult() {
        // Arrange
        Integer year = 2022;
        Integer month = 1;
        when(medicineRepository.statsByPrognosisMedicine(year, month)).thenReturn(new ArrayList<>());
        
        // Act
        List<Object[]> result = statsService.statsByPrognosisMedicine(year, month);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        // Verify repository method was called with correct parameters
        verify(medicineRepository, times(1)).statsByPrognosisMedicine(year, month);
    }
    
    /**
     * Test case: TC_STATS_08
     * Test paginated statistics for user MRL and ME history with empty result
     * Input: Page, size, and user parameters with no data
     * Expected output: Empty paginated list
     * 
     * Mục tiêu: Kiểm tra thống kê phân trang lịch sử khám bệnh của người dùng với kết quả rỗng
     * Đầu vào: Tham số trang, kích thước trang và người dùng không có dữ liệu
     * Đầu ra mong đợi: Danh sách phân trang rỗng
     */
    @Test
    @DisplayName("TC_STATS_08: Test paginated statistics for user MRL and ME history with empty result")
    @Rollback(true)
    public void testPaginatedStatsUserMrlAndMeHistory_EmptyResult() {
        // Arrange
        Integer page = 1;
        Integer size = 10;
        when(medicalRegistryListRepository.statsUserMrlAndMeHistory(testUser)).thenReturn(new ArrayList<>());
        
        // Act
        Page<MrlAndMeHistoryDto> result = statsService.paginatedStatsUserMrlAndMeHistory(page, size, testUser);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
        
        // Verify repository method was called with correct parameters
        verify(medicalRegistryListRepository, times(1)).statsUserMrlAndMeHistory(testUser);
    }
    
    /**
     * Test case: TC_STATS_09
     * Test sorting empty payment history
     * Input: Empty list of payment history DTOs
     * Expected output: Empty sorted list
     * 
     * Mục tiêu: Kiểm tra sắp xếp danh sách lịch sử thanh toán rỗng
     * Đầu vào: Danh sách DTO lịch sử thanh toán rỗng
     * Đầu ra mong đợi: Danh sách rỗng
     */
    @Test
    @DisplayName("TC_STATS_09: Test sorting empty payment history")
    @Rollback(true)
    public void testSortByCreatedDate_EmptyList() {
        // Arrange
        List<PaymentHistoryDto> emptyList = new ArrayList<>();
        
        // Act
        List<PaymentHistoryDto> result = statsService.sortByCreatedDate(emptyList);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
