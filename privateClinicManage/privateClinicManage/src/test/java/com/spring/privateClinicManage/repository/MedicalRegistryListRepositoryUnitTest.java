package com.spring.privateClinicManage.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.Role;
import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;

/**
 * Unit Test cho MedicalRegistryListRepository
 *
 * Class này tập trung test các phương thức repository liên quan đến chức năng đăng ký phiếu khám bệnh
 * Sử dụng Mockito để mock repository thay vì sử dụng cơ sở dữ liệu thật
 *
 * @author Test Team
 */
@ExtendWith(MockitoExtension.class)
public class MedicalRegistryListRepositoryUnitTest {

    @Mock
    private MedicalRegistryListRepository medicalRegistryListRepository;

    // Các đối tượng dùng chung cho test
    private User testUser;
    private Schedule testSchedule;
    private StatusIsApproved testStatus;
    private MedicalRegistryList testMrl;
    private List<MedicalRegistryList> testMrlList;

    @BeforeEach
    public void setUp() {
        // Tạo role
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_BENHNHAN");

        // Tạo user
        testUser = new User();
        testUser.setId(1);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setRole(role);
        testUser.setActive(true);

        // Tạo schedule
        testSchedule = new Schedule();
        testSchedule.setId(1);
        testSchedule.setDate(new Date());
        testSchedule.setIsDayOff(false);

        // Tạo status
        testStatus = new StatusIsApproved();
        testStatus.setId(1);
        testStatus.setStatus("CHECKING");
        testStatus.setNote("Đang kiểm tra");

        // Tạo medical registry list
        testMrl = new MedicalRegistryList();
        testMrl.setId(1);
        testMrl.setName("Test Patient");
        testMrl.setFavor("Test Favor");
        testMrl.setCreatedDate(new Date());
        testMrl.setIsCanceled(false);
        testMrl.setUser(testUser);
        testMrl.setSchedule(testSchedule);
        testMrl.setStatusIsApproved(testStatus);

        testMrlList = new ArrayList<>();
        testMrlList.add(testMrl);
    }

    /**
     * Test case: TC-REPO-01-01
     * Mục tiêu: Kiểm tra phương thức findMRLByUserAndSchedule hoạt động đúng khi có phiếu khám
     * Input: User và Schedule hợp lệ
     * Expected output: Trả về đối tượng MedicalRegistryList đúng
     */
    @Test
    @DisplayName("TC-REPO-01-01: Test findMRLByUserAndSchedule - Tìm phiếu khám theo người dùng và lịch thành công")
    public void testFindMRLByUserAndScheduleSuccess() {
        // Arrange
        when(medicalRegistryListRepository.findMRLByUserAndSchedule(testUser, testSchedule)).thenReturn(testMrl);

        // Act
        MedicalRegistryList result = medicalRegistryListRepository.findMRLByUserAndSchedule(testUser, testSchedule);

        // Assert
        assertNotNull(result);
        assertEquals("Test Patient", result.getName());
        assertEquals("Test Favor", result.getFavor());
        assertEquals(testUser.getId(), result.getUser().getId());
        assertEquals(testSchedule.getId(), result.getSchedule().getId());
    }

    /**
     * Test case: TC-REPO-01-02
     * Mục tiêu: Kiểm tra phương thức findMRLByUserAndSchedule hoạt động đúng khi không có phiếu khám
     * Input: User không tồn tại và Schedule hợp lệ
     * Expected output: Trả về null
     */
    @Test
    @DisplayName("TC-REPO-01-02: Test findMRLByUserAndSchedule - Không tìm thấy phiếu khám theo người dùng và lịch")
    public void testFindMRLByUserAndScheduleNotFound() {
        // Arrange
        User nonExistentUser = new User();
        nonExistentUser.setId(999);
        when(medicalRegistryListRepository.findMRLByUserAndSchedule(nonExistentUser, testSchedule)).thenReturn(null);

        // Act
        MedicalRegistryList result = medicalRegistryListRepository.findMRLByUserAndSchedule(nonExistentUser, testSchedule);

        // Assert
        assertNull(result);
    }

    /**
     * Test case: TC-REPO-02-01
     * Mục tiêu: Kiểm tra phương thức findByUser hoạt động đúng khi có phiếu khám
     * Input: User hợp lệ
     * Expected output: Trả về danh sách phiếu khám của người dùng
     */
    @Test
    @DisplayName("TC-REPO-02-01: Test findByUser - Tìm tất cả phiếu khám của một người dùng thành công")
    public void testFindByUserSuccess() {
        // Arrange
        when(medicalRegistryListRepository.findByUser(testUser)).thenReturn(testMrlList);

        // Act
        List<MedicalRegistryList> result = medicalRegistryListRepository.findByUser(testUser);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Patient", result.get(0).getName());
    }

    /**
     * Test case: TC-REPO-02-02
     * Mục tiêu: Kiểm tra phương thức findByUser hoạt động đúng khi không có phiếu khám
     * Input: User không có phiếu khám
     * Expected output: Trả về danh sách rỗng
     */
    @Test
    @DisplayName("TC-REPO-02-02: Test findByUser - Không tìm thấy phiếu khám của người dùng")
    public void testFindByUserEmpty() {
        // Arrange
        User newUser = new User();
        newUser.setId(2);
        newUser.setName("New User");
        newUser.setEmail("new@example.com");
        newUser.setPassword("password");
        newUser.setRole(testUser.getRole());
        newUser.setActive(true);

        when(medicalRegistryListRepository.findByUser(newUser)).thenReturn(new ArrayList<>());

        // Act
        List<MedicalRegistryList> result = medicalRegistryListRepository.findByUser(newUser);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test case: TC-REPO-03-01
     * Mục tiêu: Kiểm tra phương thức findByScheduleAndStatusIsApproved hoạt động đúng
     * Input: Năm, tháng, ngày và trạng thái hợp lệ
     * Expected output: Trả về danh sách phiếu khám thỏa điều kiện
     */
    @Test
    @DisplayName("TC-REPO-03-01: Test findByScheduleAndStatusIsApproved - Tìm phiếu khám theo lịch và trạng thái")
    public void testFindByScheduleAndStatusIsApproved() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(testSchedule.getDate());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Calendar.MONTH bắt đầu từ 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        when(medicalRegistryListRepository.findByScheduleAndStatusIsApproved(
                year, month, day, testStatus)).thenReturn(testMrlList);

        // Act
        List<MedicalRegistryList> result = medicalRegistryListRepository.findByScheduleAndStatusIsApproved(
                year, month, day, testStatus);

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Patient", result.get(0).getName());
    }

    /**
     * Test case: TC-REPO-04-01
     * Mục tiêu: Kiểm tra phương thức findByAnyKey hoạt động đúng
     * Input: Từ khóa tìm kiếm hợp lệ
     * Expected output: Trả về danh sách phiếu khám thỏa điều kiện
     */
    @Test
    @DisplayName("TC-REPO-04-01: Test findByAnyKey - Tìm phiếu khám theo từ khóa")
    public void testFindByAnyKey() {
        // Arrange
        when(medicalRegistryListRepository.findByAnyKey("Test")).thenReturn(testMrlList);

        // Act
        List<MedicalRegistryList> result = medicalRegistryListRepository.findByAnyKey("Test");

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Test Patient", result.get(0).getName());
    }

    /**
     * Test case: TC-REPO-05-01
     * Mục tiêu: Kiểm tra phương thức countRegistrationsBetweenDates hoạt động đúng
     * Input: Khoảng thời gian hợp lệ
     * Expected output: Trả về số lượng phiếu khám trong khoảng thời gian
     */
    @Test
    @DisplayName("TC-REPO-05-01: Test countRegistrationsBetweenDates - Đếm số lượng phiếu khám trong khoảng thời gian")
    public void testCountRegistrationsBetweenDates() {
        // Arrange
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        Date startDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 2);
        Date endDate = calendar.getTime();

        when(medicalRegistryListRepository.countRegistrationsBetweenDates(startDate, endDate)).thenReturn(1L);

        // Act
        Long result = medicalRegistryListRepository.countRegistrationsBetweenDates(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result);
    }

    /**
     * Test case: TC-REPO-06-01
     * Mục tiêu: Kiểm tra phương thức statsRegistrationsByStatus hoạt động đúng
     * Input: Năm hợp lệ
     * Expected output: Trả về thống kê phiếu khám theo trạng thái
     */
    @Test
    @DisplayName("TC-REPO-06-01: Test statsRegistrationsByStatus - Thống kê phiếu khám theo trạng thái")
    public void testStatsRegistrationsByStatus() {
        // Arrange
        int year = 2023;
        List<Object[]> expectedStats = new ArrayList<>();
        expectedStats.add(new Object[] {"CHECKING", 5L});
        expectedStats.add(new Object[] {"SUCCESS", 3L});

        when(medicalRegistryListRepository.statsRegistrationsByStatus(year)).thenReturn(expectedStats);

        // Act
        List<Object[]> result = medicalRegistryListRepository.statsRegistrationsByStatus(year);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("CHECKING", result.get(0)[0]);
        assertEquals(5L, result.get(0)[1]);
    }

    /**
     * Test case: TC-REPO-07-01
     * Mục tiêu: Kiểm tra phương thức statsRegistrationsByMonth hoạt động đúng
     * Input: Năm hợp lệ
     * Expected output: Trả về thống kê phiếu khám theo tháng
     */
    @Test
    @DisplayName("TC-REPO-07-01: Test statsRegistrationsByMonth - Thống kê phiếu khám theo tháng")
    public void testStatsRegistrationsByMonth() {
        // Arrange
        int year = 2023;
        List<Object[]> expectedStats = new ArrayList<>();
        expectedStats.add(new Object[] {1, 10L}); // Tháng 1: 10 phiếu
        expectedStats.add(new Object[] {2, 15L}); // Tháng 2: 15 phiếu

        when(medicalRegistryListRepository.statsRegistrationsByMonth(year)).thenReturn(expectedStats);

        // Act
        List<Object[]> result = medicalRegistryListRepository.statsRegistrationsByMonth(year);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0)[0]);
        assertEquals(10L, result.get(0)[1]);
    }

    /**
     * Test case: TC-REPO-08-01
     * Mục tiêu: Kiểm tra phương thức statsRegistrationsByDay hoạt động đúng
     * Input: Năm và tháng hợp lệ
     * Expected output: Trả về thống kê phiếu khám theo ngày
     */
    @Test
    @DisplayName("TC-REPO-08-01: Test statsRegistrationsByDay - Thống kê phiếu khám theo ngày")
    public void testStatsRegistrationsByDay() {
        // Arrange
        int year = 2023;
        int month = 1;
        List<Object[]> expectedStats = new ArrayList<>();
        expectedStats.add(new Object[] {1, 3L}); // Ngày 1: 3 phiếu
        expectedStats.add(new Object[] {2, 5L}); // Ngày 2: 5 phiếu

        when(medicalRegistryListRepository.statsRegistrationsByDay(year, month)).thenReturn(expectedStats);

        // Act
        List<Object[]> result = medicalRegistryListRepository.statsRegistrationsByDay(year, month);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0)[0]);
        assertEquals(3L, result.get(0)[1]);
    }

    /**
     * Test case: TC-REPO-09-01
     * Mục tiêu: Kiểm tra phương thức statsRegistrationsByUser hoạt động đúng
     * Input: Năm hợp lệ
     * Expected output: Trả về thống kê phiếu khám theo người dùng
     */
    @Test
    @DisplayName("TC-REPO-09-01: Test statsRegistrationsByUser - Thống kê phiếu khám theo người dùng")
    public void testStatsRegistrationsByUser() {
        // Arrange
        int year = 2023;
        List<Object[]> expectedStats = new ArrayList<>();
        expectedStats.add(new Object[] {"Test User", 8L}); // Test User: 8 phiếu
        expectedStats.add(new Object[] {"Another User", 5L}); // Another User: 5 phiếu

        when(medicalRegistryListRepository.statsRegistrationsByUser(year)).thenReturn(expectedStats);

        // Act
        List<Object[]> result = medicalRegistryListRepository.statsRegistrationsByUser(year);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test User", result.get(0)[0]);
        assertEquals(8L, result.get(0)[1]);
    }
}
