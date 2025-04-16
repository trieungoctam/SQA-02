package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.MedicalRegistryListRepository;
import com.spring.privateClinicManage.service.impl.MedicalRegistryListServiceImpl;

/**
 * Unit Test cho MedicalRegistryListService
 * 
 * Class này tập trung test các phương thức liên quan đến chức năng đăng ký phiếu khám bệnh
 * 
 * @author Test Team
 */
@ExtendWith(MockitoExtension.class)
public class MedicalRegistryListServiceUnitTest {

    @Mock
    private MedicalRegistryListRepository medicalRegistryListRepository;

    @InjectMocks
    private MedicalRegistryListServiceImpl medicalRegistryListService;

    // Các đối tượng dùng chung cho test
    private User testUser;
    private Schedule testSchedule;
    private StatusIsApproved testStatus;
    private MedicalRegistryList testMrl;
    private List<MedicalRegistryList> testMrlList;

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

        testMrlList = new ArrayList<>();
        testMrlList.add(testMrl);
    }

    /**
     * Test case: TC-SRV-01
     * Mục tiêu: Kiểm tra phương thức saveMedicalRegistryList hoạt động đúng
     * Input: Đối tượng MedicalRegistryList hợp lệ
     * Expected output: Repository save được gọi một lần với đối tượng đúng
     */
    @Test
    @DisplayName("TC-SRV-01: Test saveMedicalRegistryList - Lưu phiếu khám bệnh thành công")
    public void testSaveMedicalRegistryList() {
        // Arrange
        when(medicalRegistryListRepository.save(any(MedicalRegistryList.class))).thenReturn(testMrl);
        
        // Act
        medicalRegistryListService.saveMedicalRegistryList(testMrl);
        
        // Assert
        verify(medicalRegistryListRepository, times(1)).save(testMrl);
    }

    /**
     * Test case: TC-SRV-02
     * Mục tiêu: Kiểm tra phương thức findById hoạt động đúng khi tìm thấy phiếu khám
     * Input: ID hợp lệ
     * Expected output: Trả về đối tượng MedicalRegistryList đúng
     */
    @Test
    @DisplayName("TC-SRV-02: Test findById - Tìm phiếu khám theo ID thành công")
    public void testFindByIdSuccess() {
        // Arrange
        when(medicalRegistryListRepository.findById(1)).thenReturn(Optional.of(testMrl));
        
        // Act
        MedicalRegistryList result = medicalRegistryListService.findById(1);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Patient", result.getName());
    }

    /**
     * Test case: TC-SRV-02
     * Mục tiêu: Kiểm tra phương thức findById hoạt động đúng khi không tìm thấy phiếu khám
     * Input: ID không tồn tại
     * Expected output: Trả về null
     */
    @Test
    @DisplayName("TC-SRV-02: Test findById - Tìm phiếu khám theo ID không thành công")
    public void testFindByIdNotFound() {
        // Arrange
        when(medicalRegistryListRepository.findById(999)).thenReturn(Optional.empty());
        
        // Act
        MedicalRegistryList result = medicalRegistryListService.findById(999);
        
        // Assert
        assertNull(result);
    }

    /**
     * Test case: TC-SRV-03
     * Mục tiêu: Kiểm tra phương thức findAllMrl hoạt động đúng
     * Input: N/A
     * Expected output: Trả về danh sách tất cả phiếu khám
     */
    @Test
    @DisplayName("TC-SRV-03: Test findAllMrl - Lấy tất cả phiếu khám")
    public void testFindAllMrl() {
        // Arrange
        when(medicalRegistryListRepository.findAll()).thenReturn(testMrlList);
        
        // Act
        List<MedicalRegistryList> result = medicalRegistryListService.findAllMrl();
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMrl.getId(), result.get(0).getId());
    }

    /**
     * Test case: TC-SRV-07
     * Mục tiêu: Kiểm tra phương thức findByScheduleAndStatusIsApproved2 hoạt động đúng khi có phiếu khám
     * Input: Schedule và StatusIsApproved hợp lệ
     * Expected output: Trả về danh sách phiếu khám thỏa điều kiện
     */
    @Test
    @DisplayName("TC-SRV-07: Test findByScheduleAndStatusIsApproved2 - Tìm phiếu khám theo lịch và trạng thái thành công")
    public void testFindByScheduleAndStatusIsApproved2Success() {
        // Arrange
        when(medicalRegistryListRepository.findByScheduleAndStatusIsApproved2(testSchedule, testStatus))
                .thenReturn(testMrlList);
        
        // Act
        List<MedicalRegistryList> result = medicalRegistryListService.findByScheduleAndStatusIsApproved2(testSchedule, testStatus);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMrl.getId(), result.get(0).getId());
    }

    /**
     * Test case: TC-SRV-07
     * Mục tiêu: Kiểm tra phương thức findByScheduleAndStatusIsApproved2 hoạt động đúng khi không có phiếu khám
     * Input: Schedule và StatusIsApproved hợp lệ nhưng không có phiếu khám thỏa điều kiện
     * Expected output: Trả về danh sách rỗng
     */
    @Test
    @DisplayName("TC-SRV-07: Test findByScheduleAndStatusIsApproved2 - Không tìm thấy phiếu khám theo lịch và trạng thái")
    public void testFindByScheduleAndStatusIsApproved2Empty() {
        // Arrange
        when(medicalRegistryListRepository.findByScheduleAndStatusIsApproved2(testSchedule, testStatus))
                .thenReturn(new ArrayList<>());
        
        // Act
        List<MedicalRegistryList> result = medicalRegistryListService.findByScheduleAndStatusIsApproved2(testSchedule, testStatus);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * Test case: TC-SRV-09
     * Mục tiêu: Kiểm tra phương thức countMRLByScheduleAndStatuses hoạt động đúng
     * Input: Schedule và danh sách StatusIsApproved hợp lệ
     * Expected output: Trả về số lượng phiếu khám thỏa điều kiện
     */
    @Test
    @DisplayName("TC-SRV-09: Test countMRLByScheduleAndStatuses - Đếm số lượng phiếu khám theo lịch và danh sách trạng thái")
    public void testCountMRLByScheduleAndStatuses() {
        // Arrange
        List<StatusIsApproved> statuses = Arrays.asList(testStatus);
        when(medicalRegistryListRepository.countMRLByScheduleAndStatuses(testSchedule, statuses))
                .thenReturn(5);
        
        // Act
        Integer result = medicalRegistryListService.countMRLByScheduleAndStatuses(testSchedule, statuses);
        
        // Assert
        assertEquals(5, result);
    }

    /**
     * Test case: TC-SRV-20
     * Mục tiêu: Kiểm tra phương thức findAllMrlByUserAndName hoạt động đúng khi có phiếu khám
     * Input: User và tên bệnh nhân hợp lệ
     * Expected output: Trả về danh sách phiếu khám thỏa điều kiện
     */
    @Test
    @DisplayName("TC-SRV-20: Test findAllMrlByUserAndName - Tìm phiếu khám theo người dùng và tên thành công")
    public void testFindAllMrlByUserAndNameSuccess() {
        // Arrange
        String patientName = "Test Patient";
        when(medicalRegistryListRepository.findAllMrlByUserAndName(testUser, patientName))
                .thenReturn(testMrlList);
        
        // Act
        List<MedicalRegistryList> result = medicalRegistryListService.findAllMrlByUserAndName(testUser, patientName);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testMrl.getId(), result.get(0).getId());
        assertEquals(patientName, result.get(0).getName());
    }

    /**
     * Test case: TC-SRV-20
     * Mục tiêu: Kiểm tra phương thức findAllMrlByUserAndName hoạt động đúng khi không có phiếu khám
     * Input: User và tên bệnh nhân hợp lệ nhưng không có phiếu khám thỏa điều kiện
     * Expected output: Trả về danh sách rỗng
     */
    @Test
    @DisplayName("TC-SRV-20: Test findAllMrlByUserAndName - Không tìm thấy phiếu khám theo người dùng và tên")
    public void testFindAllMrlByUserAndNameEmpty() {
        // Arrange
        String patientName = "Non-existent Patient";
        when(medicalRegistryListRepository.findAllMrlByUserAndName(testUser, patientName))
                .thenReturn(new ArrayList<>());
        
        // Act
        List<MedicalRegistryList> result = medicalRegistryListService.findAllMrlByUserAndName(testUser, patientName);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
