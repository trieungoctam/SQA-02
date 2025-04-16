package com.spring.privateClinicManage.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.spring.privateClinicManage.dto.MrlAndMeHistoryDto;
import com.spring.privateClinicManage.dto.PaymentHistoryDto;
import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.PaymentDetailPhase1;
import com.spring.privateClinicManage.entity.PaymentDetailPhase2;
import com.spring.privateClinicManage.entity.Role;
import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;

/**
 * Unit tests for MedicalRegistryListRepository
 * This class tests the medical registry list repository with database operations
 *
 * Các test case kiểm tra chức năng truy vấn và thống kê dữ liệu đăng ký khám bệnh:
 * - Thống kê lịch sử khám bệnh của người dùng
 * - Thống kê lịch sử thanh toán giai đoạn 1
 * - Thống kê lịch sử thanh toán giai đoạn 2
 * - Thống kê số lượng phiếu khám bệnh theo trạng thái
 *
 * Tất cả các test case đều sử dụng @Rollback(true) để đảm bảo dữ liệu test không ảnh hưởng đến database
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MedicalRegistryListRepositoryTest {

    @Autowired
    private MedicalRegistryListRepository medicalRegistryListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private StatusIsApprovedRepository statusIsApprovedRepository;

    @Autowired
    private MedicalExaminationRepository medicalExaminationRepository;

    @Autowired
    private PaymentDetailPhase1Repository paymentDetailPhase1Repository;

    @Autowired
    private PaymentDetailPhase2Repository paymentDetailPhase2Repository;

    // Test data
    private User testUser;
    private Role testRole;
    private Schedule testSchedule;
    private StatusIsApproved testStatus;
    private MedicalRegistryList testMrl;
    private MedicalExamination testMedicalExam;
    private PaymentDetailPhase1 testPaymentPhase1;
    private PaymentDetailPhase2 testPaymentPhase2;

    @BeforeEach
    public void setUp() {
        // Create test role
        testRole = new Role();
        testRole.setName("ROLE_BENHNHAN");
        testRole = roleRepository.save(testRole);

        // Create test user
        testUser = new User();
        testUser.setName("Test Patient");
        testUser.setEmail("test.patient@example.com");
        testUser.setPassword("password");
        testUser.setPhone("0123456789");
        testUser.setAddress("Test Address");
        testUser.setGender("Male");
        testUser.setAvatar("default.jpg");
        testUser.setRole(testRole);
        testUser.setActive(true);
        testUser = userRepository.save(testUser);

        // Create test schedule
        testSchedule = new Schedule();
        testSchedule.setDate(new Date());
        testSchedule.setIsDayOff(false);
        testSchedule.setDescription("Test Schedule");
        testSchedule = scheduleRepository.save(testSchedule);

        // Create test status
        testStatus = new StatusIsApproved();
        testStatus.setStatus("CHECKING");
        testStatus.setNote("Đang chờ phê duyệt");
        testStatus = statusIsApprovedRepository.save(testStatus);

        // Create test medical registry list
        testMrl = new MedicalRegistryList();
        testMrl.setName("Test Patient");
        testMrl.setUser(testUser);
        testMrl.setSchedule(testSchedule);
        testMrl.setStatusIsApproved(testStatus);
        testMrl.setCreatedDate(new Date());
        testMrl.setFavor("Test Favor"); // Required field
        testMrl.setIsCanceled(false); // Required field
        testMrl.setOrder(1);
        testMrl = medicalRegistryListRepository.save(testMrl);

        // Create test medical examination
        testMedicalExam = new MedicalExamination();
        testMedicalExam.setMrl(testMrl);
        testMedicalExam.setCreatedDate(new Date());
        testMedicalExam.setSymptomProcess("Test Symptom"); // Using the correct field name
        testMedicalExam.setTreatmentProcess("Test Treatment"); // Required field
        testMedicalExam = medicalExaminationRepository.save(testMedicalExam);

        // Create test payment phase 1
        testPaymentPhase1 = new PaymentDetailPhase1();
        testPaymentPhase1.setOrderId("ORD001");
        testPaymentPhase1.setCreatedDate(new Date());
        testPaymentPhase1.setAmount(100000L);
        testPaymentPhase1.setDescription("Test Payment Phase 1");
        testPaymentPhase1.setResultCode("00");
        testPaymentPhase1.setPartnerCode("MOMO");
        // Save first to get ID
        testPaymentPhase1 = paymentDetailPhase1Repository.save(testPaymentPhase1);

        // Update MRL to reference the payment
        testMrl.setPaymentPhase1(testPaymentPhase1);
        testMrl = medicalRegistryListRepository.save(testMrl);

        // Create test payment phase 2
        testPaymentPhase2 = new PaymentDetailPhase2();
        testPaymentPhase2.setOrderId("ORD002");
        testPaymentPhase2.setCreatedDate(new Date());
        testPaymentPhase2.setAmount(200000L);
        testPaymentPhase2.setDescription("Test Payment Phase 2");
        testPaymentPhase2.setResultCode("00");
        testPaymentPhase2.setPartnerCode("VNPAY");
        // Save first to get ID
        testPaymentPhase2 = paymentDetailPhase2Repository.save(testPaymentPhase2);

        // Update MedicalExamination to reference the payment
        testMedicalExam.setPaymentPhase2(testPaymentPhase2);
        testMedicalExam = medicalExaminationRepository.save(testMedicalExam);
    }

    /**
     * Test case: TC_REPO_MRL_01
     * Test statistics for user MRL and ME history
     * Input: User with medical registry list and examination
     * Expected output: List of MrlAndMeHistoryDto with correct data
     *
     * Mục tiêu: Kiểm tra thống kê lịch sử khám bệnh của người dùng
     * Đầu vào: Người dùng có phiếu đăng ký khám bệnh và phiếu khám bệnh
     * Đầu ra mong đợi: Danh sách MrlAndMeHistoryDto với dữ liệu chính xác
     *
     * @Rollback(true) - Đảm bảo dữ liệu test sẽ được rollback sau khi test hoàn thành
     * CheckDB: Kiểm tra kết quả thống kê từ database có đúng với dữ liệu đã tạo không
     */
    @Test
    @Rollback(true)
    @DisplayName("TC_REPO_MRL_01: Test statistics for user MRL and ME history")
    public void testStatsUserMrlAndMeHistory() {
        // Act
        List<MrlAndMeHistoryDto> result = medicalRegistryListRepository.statsUserMrlAndMeHistory(testUser);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        MrlAndMeHistoryDto dto = result.get(0);
        assertEquals("Test Patient", dto.getName());
        assertNotNull(dto.getLastestDate());
        assertEquals(1L, dto.getTotal());
    }

    /**
     * Test case: TC_REPO_MRL_02
     * Test statistics for payment phase 1 history
     * Input: Patient name with payment phase 1 record
     * Expected output: List of PaymentHistoryDto with correct data
     *
     * Mục tiêu: Kiểm tra thống kê lịch sử thanh toán giai đoạn 1
     * Đầu vào: Tên bệnh nhân có bản ghi thanh toán giai đoạn 1
     * Đầu ra mong đợi: Danh sách PaymentHistoryDto với dữ liệu chính xác
     *
     * @Rollback(true) - Đảm bảo dữ liệu test sẽ được rollback sau khi test hoàn thành
     * CheckDB: Kiểm tra kết quả thống kê từ database có đúng với dữ liệu đã tạo không
     */
    @Test
    @Rollback(true)
    @DisplayName("TC_REPO_MRL_02: Test statistics for payment phase 1 history")
    public void testStatsPaymentPhase1History() {
        // Act
        List<PaymentHistoryDto> result = medicalRegistryListRepository.statsPaymentPhase1History("Test Patient");

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        PaymentHistoryDto dto = result.get(0);
        assertEquals("ORD001", dto.getOrderId());
        assertEquals("Test Patient", dto.getName());
        assertEquals(100000L, dto.getAmount());
        assertEquals("Test Payment Phase 1", dto.getDescription());
        assertEquals("00", dto.getResultCode());
        assertEquals("MOMO", dto.getPartnerCode());
    }

    /**
     * Test case: TC_REPO_MRL_03
     * Test statistics for payment phase 2 history
     * Input: Patient name with payment phase 2 record
     * Expected output: List of PaymentHistoryDto with correct data
     *
     * Mục tiêu: Kiểm tra thống kê lịch sử thanh toán giai đoạn 2
     * Đầu vào: Tên bệnh nhân có bản ghi thanh toán giai đoạn 2
     * Đầu ra mong đợi: Danh sách PaymentHistoryDto với dữ liệu chính xác
     *
     * @Rollback(true) - Đảm bảo dữ liệu test sẽ được rollback sau khi test hoàn thành
     * CheckDB: Kiểm tra kết quả thống kê từ database có đúng với dữ liệu đã tạo không
     */
    @Test
    @Rollback(true)
    @DisplayName("TC_REPO_MRL_03: Test statistics for payment phase 2 history")
    public void testStatsPaymentPhase2History() {
        // Act
        List<PaymentHistoryDto> result = medicalRegistryListRepository.statsPaymentPhase2History("Test Patient");

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        PaymentHistoryDto dto = result.get(0);
        assertEquals("ORD002", dto.getOrderId());
        assertEquals("Test Patient", dto.getName());
        assertEquals(200000L, dto.getAmount());
        assertEquals("Test Payment Phase 2", dto.getDescription());
        assertEquals("00", dto.getResultCode());
        assertEquals("VNPAY", dto.getPartnerCode());
    }

    /**
     * Test case: TC_REPO_MRL_04
     * Test statistics for registrations by status
     * Input: Year with registration records
     * Expected output: List of status counts with correct data
     *
     * Mục tiêu: Kiểm tra thống kê số lượng phiếu khám bệnh theo trạng thái
     * Đầu vào: Năm có bản ghi đăng ký khám bệnh
     * Đầu ra mong đợi: Danh sách số lượng theo trạng thái với dữ liệu chính xác
     *
     * @Rollback(true) - Đảm bảo dữ liệu test sẽ được rollback sau khi test hoàn thành
     * CheckDB: Kiểm tra kết quả thống kê từ database có đúng với dữ liệu đã tạo không
     */
    @Test
    @Rollback(true)
    @DisplayName("TC_REPO_MRL_04: Test statistics for registrations by status")
    public void testStatsRegistrationsByStatus() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);

        // Act
        List<Object[]> result = medicalRegistryListRepository.statsRegistrationsByStatus(currentYear);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Find the CHECKING status in the results
        boolean foundCheckingStatus = false;
        for (Object[] row : result) {
            if ("CHECKING".equals(row[0])) {
                foundCheckingStatus = true;
                assertEquals(1L, row[1]); // Should have 1 record with CHECKING status
                break;
            }
        }

        assertTrue(foundCheckingStatus, "Should find CHECKING status in the results");
    }

    /**
     * Test case: TC_REPO_MRL_05
     * Test statistics for registrations by month
     * Input: Year with registration records
     * Expected output: List of month counts with correct data
     *
     * Mục tiêu: Kiểm tra thống kê số lượng phiếu khám bệnh theo tháng
     * Đầu vào: Năm có bản ghi đăng ký khám bệnh
     * Đầu ra mong đợi: Danh sách số lượng theo tháng với dữ liệu chính xác
     *
     * @Rollback(true) - Đảm bảo dữ liệu test sẽ được rollback sau khi test hoàn thành
     * CheckDB: Kiểm tra kết quả thống kê từ database có đúng với dữ liệu đã tạo không
     */
    @Test
    @Rollback(true)
    @DisplayName("TC_REPO_MRL_05: Test statistics for registrations by month")
    public void testStatsRegistrationsByMonth() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH) + 1; // Calendar months are 0-based

        // Act
        List<Object[]> result = medicalRegistryListRepository.statsRegistrationsByMonth(currentYear);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Find the current month in the results
        boolean foundCurrentMonth = false;
        for (Object[] row : result) {
            if (currentMonth == ((Number) row[0]).intValue()) {
                foundCurrentMonth = true;
                assertEquals(2L, row[1]); // Should have 2 records in the current month (testMrl and another record)
                break;
            }
        }

        assertTrue(foundCurrentMonth, "Should find current month in the results");
    }

    /**
     * Test case: TC_REPO_MRL_06
     * Test statistics for user with no medical records
     * Input: User with no medical registry list
     * Expected output: Empty list
     *
     * Mục tiêu: Kiểm tra thống kê với người dùng không có bản ghi khám bệnh
     * Đầu vào: Người dùng không có phiếu đăng ký khám bệnh
     * Đầu ra mong đợi: Danh sách rỗng
     *
     * @Rollback(true) - Đảm bảo dữ liệu test sẽ được rollback sau khi test hoàn thành
     */
    @Test
    @Rollback(true)
    @DisplayName("TC_REPO_MRL_06: Test statistics for user with no medical records")
    public void testStatsUserMrlAndMeHistory_NoRecords() {
        // Arrange
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("new.user@example.com");
        newUser.setPassword("password");
        newUser.setPhone("9876543210");
        newUser.setAddress("New Address");
        newUser.setGender("Female");
        newUser.setAvatar("default.jpg");
        newUser.setRole(testRole);
        newUser.setActive(true);
        newUser = userRepository.save(newUser);

        // Act
        List<MrlAndMeHistoryDto> result = medicalRegistryListRepository.statsUserMrlAndMeHistory(newUser);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
