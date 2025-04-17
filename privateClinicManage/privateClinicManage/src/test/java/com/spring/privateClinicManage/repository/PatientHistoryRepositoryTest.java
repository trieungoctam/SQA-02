package com.spring.privateClinicManage.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.spring.privateClinicManage.entity.User;

/**
 * Repository tests for patient history related queries
 * These tests verify that the repository layer correctly retrieves
 * patient history data from the database.
 *
 * Note: This test uses an actual database connection, so it's important
 * to use @Rollback to prevent test data from persisting.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PatientHistoryRepositoryTest {

    @Autowired
    private MedicalRegistryListRepository medicalRegistryListRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MedicalExaminationRepository medicalExaminationRepository;

    @Autowired
    private PaymentDetailPhase1Repository paymentDetailPhase1Repository;

    @Autowired
    private PaymentDetailPhase2Repository paymentDetailPhase2Repository;

    private User testUser;
    private MedicalRegistryList testMrl1;
    private MedicalRegistryList testMrl2;
    private PaymentDetailPhase1 testPaymentPhase1;
    private PaymentDetailPhase2 testPaymentPhase2;
    private MedicalExamination testMedicalExamination;

    @BeforeEach
    public void setup() {
        // Create test role
        Role patientRole = new Role();
        patientRole.setName("ROLE_BENHNHAN");
        patientRole = roleRepository.save(patientRole);

        // Create test user
        testUser = new User();
        testUser.setName("Test Patient");
        testUser.setEmail("test.patient@example.com");
        testUser.setPassword("password");
        testUser.setRole(patientRole);
        testUser.setActive(true);
        testUser = userRepository.save(testUser);

        // Create test payment phase 1
        testPaymentPhase1 = new PaymentDetailPhase1();
        testPaymentPhase1.setOrderId("ORD001");
        testPaymentPhase1.setCreatedDate(new Date());
        testPaymentPhase1.setAmount(100000L);
        testPaymentPhase1.setDescription("Test payment phase 1");
        testPaymentPhase1.setResultCode("00");
        testPaymentPhase1.setPartnerCode("MOMO");
        testPaymentPhase1 = paymentDetailPhase1Repository.save(testPaymentPhase1);

        // Create test medical registry list 1
        testMrl1 = new MedicalRegistryList();
        testMrl1.setName("Test Patient Record 1");
        testMrl1.setUser(testUser);
        testMrl1.setCreatedDate(new Date());
        testMrl1.setPaymentPhase1(testPaymentPhase1);
        testMrl1 = medicalRegistryListRepository.save(testMrl1);

        // Create test medical examination
        testMedicalExamination = new MedicalExamination();
        testMedicalExamination.setCreatedDate(new Date());
        testMedicalExamination.setPredict("Test prediction");
        testMedicalExamination.setAdvance("Test advance");
        testMedicalExamination.setSymptomProcess("Test symptom process");
        testMedicalExamination.setTreatmentProcess("Test treatment process");
        testMedicalExamination.setDurationDay(7);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 14);
        testMedicalExamination.setFollowUpDate(calendar.getTime());
        testMedicalExamination.setMrl(testMrl1);
        testMedicalExamination = medicalExaminationRepository.save(testMedicalExamination);

        // Create test payment phase 2
        testPaymentPhase2 = new PaymentDetailPhase2();
        testPaymentPhase2.setOrderId("ORD002");
        testPaymentPhase2.setCreatedDate(new Date());
        testPaymentPhase2.setAmount(200000L);
        testPaymentPhase2.setDescription("Test payment phase 2");
        testPaymentPhase2.setResultCode("00");
        testPaymentPhase2.setPartnerCode("VNPAY");
        testPaymentPhase2 = paymentDetailPhase2Repository.save(testPaymentPhase2);

        // Set the payment in the medical examination
        testMedicalExamination.setPaymentPhase2(testPaymentPhase2);
        testMedicalExamination = medicalExaminationRepository.save(testMedicalExamination);

        // Create test medical registry list 2 (without medical examination)
        testMrl2 = new MedicalRegistryList();
        testMrl2.setName("Test Patient Record 2");
        testMrl2.setUser(testUser);
        testMrl2.setCreatedDate(new Date());
        testMrl2 = medicalRegistryListRepository.save(testMrl2);
    }

    /**
     * Test case: TC_PATIENT_HISTORY_REPO_01
     * Test retrieving user medical history statistics
     * Input: User entity
     * Expected output: List of MrlAndMeHistoryDto containing patient history statistics
     *
     * Mục tiêu: Kiểm tra truy vấn thống kê lịch sử khám bệnh của người dùng
     * Đầu vào: Đối tượng User
     * Đầu ra mong đợi: Danh sách MrlAndMeHistoryDto chứa thống kê lịch sử bệnh nhân
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_REPO_01: Test retrieving user medical history statistics")
    @Rollback(true)
    public void testStatsUserMrlAndMeHistory() {
        // Act
        List<MrlAndMeHistoryDto> result = medicalRegistryListRepository.statsUserMrlAndMeHistory(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        // Find the record with medical examination
        MrlAndMeHistoryDto recordWithME = null;
        MrlAndMeHistoryDto recordWithoutME = null;

        for (MrlAndMeHistoryDto dto : result) {
            if (dto.getName().equals("Test Patient Record 1")) {
                recordWithME = dto;
            } else if (dto.getName().equals("Test Patient Record 2")) {
                recordWithoutME = dto;
            }
        }

        assertNotNull(recordWithME, "Should find record with medical examination");
        assertNotNull(recordWithoutME, "Should find record without medical examination");

        assertEquals(1L, recordWithME.getTotal());
        assertEquals(0L, recordWithoutME.getTotal());
        assertNotNull(recordWithME.getLastestDate());
    }

    /**
     * Test case: TC_PATIENT_HISTORY_REPO_02
     * Test retrieving payment phase 1 history
     * Input: Patient name
     * Expected output: List of PaymentHistoryDto containing phase 1 payment history
     *
     * Mục tiêu: Kiểm tra truy vấn lịch sử thanh toán giai đoạn 1
     * Đầu vào: Tên bệnh nhân
     * Đầu ra mong đợi: Danh sách PaymentHistoryDto chứa lịch sử thanh toán giai đoạn 1
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_REPO_02: Test retrieving payment phase 1 history")
    @Rollback(true)
    public void testStatsPaymentPhase1History() {
        // Act
        List<PaymentHistoryDto> result = medicalRegistryListRepository.statsPaymentPhase1History("Test Patient Record 1");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        PaymentHistoryDto paymentHistory = result.get(0);
        assertEquals("ORD001", paymentHistory.getOrderId());
        assertEquals(100000L, paymentHistory.getAmount());
        assertEquals("Test payment phase 1", paymentHistory.getDescription());
        assertEquals("00", paymentHistory.getResultCode());
        assertEquals("MOMO", paymentHistory.getPartnerCode());
    }

    /**
     * Test case: TC_PATIENT_HISTORY_REPO_03
     * Test retrieving payment phase 2 history
     * Input: Patient name
     * Expected output: List of PaymentHistoryDto containing phase 2 payment history
     *
     * Mục tiêu: Kiểm tra truy vấn lịch sử thanh toán giai đoạn 2
     * Đầu vào: Tên bệnh nhân
     * Đầu ra mong đợi: Danh sách PaymentHistoryDto chứa lịch sử thanh toán giai đoạn 2
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_REPO_03: Test retrieving payment phase 2 history")
    @Rollback(true)
    public void testStatsPaymentPhase2History() {
        // Act
        List<PaymentHistoryDto> result = medicalRegistryListRepository.statsPaymentPhase2History("Test Patient Record 1");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        PaymentHistoryDto paymentHistory = result.get(0);
        assertEquals("ORD002", paymentHistory.getOrderId());
        assertEquals(200000L, paymentHistory.getAmount());
        assertEquals("Test payment phase 2", paymentHistory.getDescription());
        assertEquals("00", paymentHistory.getResultCode());
        assertEquals("VNPAY", paymentHistory.getPartnerCode());
    }

    /**
     * Test case: TC_PATIENT_HISTORY_REPO_04
     * Test retrieving medical registry lists by user and name
     * Input: User entity and patient name
     * Expected output: List of MedicalRegistryList matching the criteria
     *
     * Mục tiêu: Kiểm tra truy vấn danh sách đăng ký khám bệnh theo người dùng và tên
     * Đầu vào: Đối tượng User và tên bệnh nhân
     * Đầu ra mong đợi: Danh sách MedicalRegistryList phù hợp với tiêu chí
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_REPO_04: Test retrieving medical registry lists by user and name")
    @Rollback(true)
    public void testFindAllMrlByUserAndName() {
        // Act
        List<MedicalRegistryList> result = medicalRegistryListRepository.findAllMrlByUserAndName(testUser, "Test Patient Record 1");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Patient Record 1", result.get(0).getName());
        assertEquals(testUser.getId(), result.get(0).getUser().getId());
    }

    /**
     * Test case: TC_PATIENT_HISTORY_REPO_05
     * Test retrieving medical examination by medical registry list
     * Input: MedicalRegistryList entity
     * Expected output: MedicalExamination entity associated with the medical registry list
     *
     * Mục tiêu: Kiểm tra truy vấn thông tin khám bệnh theo phiếu đăng ký khám
     * Đầu vào: Đối tượng MedicalRegistryList
     * Đầu ra mong đợi: Đối tượng MedicalExamination liên kết với phiếu đăng ký khám
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_REPO_05: Test retrieving medical examination by medical registry list")
    @Rollback(true)
    public void testFindByMrl() {
        // Act
        MedicalExamination result = medicalExaminationRepository.findByMrl(testMrl1);

        // Assert
        assertNotNull(result);
        assertEquals(testMedicalExamination.getId(), result.getId());
        assertEquals("Test prediction", result.getPredict());
        assertEquals("Test advance", result.getAdvance());
        assertEquals("Test symptom process", result.getSymptomProcess());
        assertEquals("Test treatment process", result.getTreatmentProcess());
        assertEquals(7, result.getDurationDay());
        assertNotNull(result.getFollowUpDate());
    }

    /**
     * Test case: TC_PATIENT_HISTORY_REPO_06
     * Test retrieving user medical history statistics with no results
     * Input: User entity with no medical history
     * Expected output: Empty list
     *
     * Mục tiêu: Kiểm tra truy vấn thống kê lịch sử khám bệnh của người dùng không có lịch sử
     * Đầu vào: Đối tượng User không có lịch sử khám bệnh
     * Đầu ra mong đợi: Danh sách rỗng
     */
    @Test
    @DisplayName("TC_PATIENT_HISTORY_REPO_06: Test retrieving user medical history statistics with no results")
    @Rollback(true)
    public void testStatsUserMrlAndMeHistoryNoResults() {
        // Create a new user with no medical history
        User newUser = new User();
        newUser.setName("New Test User");
        newUser.setEmail("new.test.user@example.com");
        newUser.setPassword("password");
        newUser.setRole(testUser.getRole());
        newUser.setActive(true);
        newUser = userRepository.save(newUser);

        // Act
        List<MrlAndMeHistoryDto> result = medicalRegistryListRepository.statsUserMrlAndMeHistory(newUser);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
