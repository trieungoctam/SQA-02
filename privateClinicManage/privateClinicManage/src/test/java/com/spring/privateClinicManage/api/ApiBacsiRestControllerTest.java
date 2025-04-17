package com.spring.privateClinicManage.api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.annotation.Rollback;

import com.spring.privateClinicManage.dto.MedicalExamDto;
import com.spring.privateClinicManage.dto.PrescriptionItemDto;
import com.spring.privateClinicManage.entity.Medicine;
import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.PrescriptionItems;
import com.spring.privateClinicManage.entity.Role;
import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.service.MedicineService;
import com.spring.privateClinicManage.service.MedicalExaminationService;
import com.spring.privateClinicManage.service.MedicalRegistryListService;
import com.spring.privateClinicManage.service.PrescriptionItemsService;
import com.spring.privateClinicManage.service.StatusIsApprovedService;
import com.spring.privateClinicManage.service.UserService;

/**
 * Unit tests for ApiBacsiRestController
 *
 * This test class covers the main functionality of the ApiBacsiRestController,
 * focusing on the medical examination creation feature.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ApiBacsiRestControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private MedicalRegistryListService medicalRegistryListService;

    @Mock
    private MedicalExaminationService medicalExaminationService;

    @Mock
    private MedicineService medicineService;

    @Mock
    private PrescriptionItemsService prescriptionItemsService;

    @Mock
    private StatusIsApprovedService statusIsApprovedService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private ApiBacsiRestController apiBacsiRestController;

    // Test data
    private User currentUser;
    private User patientUser;
    private MedicalRegistryList testMrl;
    private MedicalExamDto validMedicalExamDto;
    private Medicine testMedicine1;
    private Medicine testMedicine2;
    private StatusIsApproved processingStatus;
    private StatusIsApproved paymentPhase2Status;
    private Date fixedDate;

    /**
     * Setup test data before each test
     */
    @BeforeEach
    public void setup() {
        // Setup fixed date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            fixedDate = sdf.parse("2023-07-15");
        } catch (ParseException e) {
            fixedDate = new Date(); // Fallback
        }

        // Mock user roles
        Role bacsiRole = new Role();
        bacsiRole.setId(1);
        bacsiRole.setName("ROLE_BACSI");

        Role benhnhanRole = new Role();
        benhnhanRole.setId(2);
        benhnhanRole.setName("ROLE_BENHNHAN");

        // Mock users
        currentUser = new User();
        currentUser.setId(1);
        currentUser.setEmail("bacsi@example.com");
        currentUser.setRole(bacsiRole);

        patientUser = new User();
        patientUser.setId(2);
        patientUser.setEmail("patient@example.com");
        patientUser.setRole(benhnhanRole);
        patientUser.setPhone("0123456789");
        patientUser.setAddress("123 Test Street");

        // Mock schedule
        Schedule testSchedule = new Schedule();
        testSchedule.setId(1);
        testSchedule.setDate(fixedDate);
        testSchedule.setIsDayOff(false);

        // Mock statuses
        processingStatus = new StatusIsApproved();
        processingStatus.setId(3);
        processingStatus.setStatus("PROCESSING");

        paymentPhase2Status = new StatusIsApproved();
        paymentPhase2Status.setId(4);
        paymentPhase2Status.setStatus("PAYMENTPHASE2");

        // Mock MedicalRegistryList
        testMrl = new MedicalRegistryList();
        testMrl.setId(1);
        testMrl.setName("Test Patient");
        testMrl.setFavor("Test symptoms");
        testMrl.setCreatedDate(fixedDate);
        testMrl.setIsCanceled(false);
        testMrl.setUser(patientUser);
        testMrl.setSchedule(testSchedule);
        testMrl.setStatusIsApproved(processingStatus);

        // Mock medicines
        testMedicine1 = new Medicine();
        testMedicine1.setId(1);
        testMedicine1.setName("Paracetamol");
        testMedicine1.setPrice(10000L);

        testMedicine2 = new Medicine();
        testMedicine2.setId(2);
        testMedicine2.setName("Amoxicillin");
        testMedicine2.setPrice(15000L);

        // Mock prescription items DTOs
        List<PrescriptionItemDto> prescriptionItemDtos = new ArrayList<>();

        PrescriptionItemDto item1 = new PrescriptionItemDto();
        item1.setId(1);
        item1.setDescription("Uống sau ăn");
        item1.setPrognosis(3); // 3 viên mỗi ngày
        prescriptionItemDtos.add(item1);

        PrescriptionItemDto item2 = new PrescriptionItemDto();
        item2.setId(2);
        item2.setDescription("Uống trước ăn");
        item2.setPrognosis(2); // 2 viên mỗi ngày
        prescriptionItemDtos.add(item2);

        // Mock MedicalExamDto
        validMedicalExamDto = new MedicalExamDto();
        validMedicalExamDto.setMrlId(1);
        validMedicalExamDto.setAdvance("Nghỉ ngơi, uống nhiều nước");
        validMedicalExamDto.setPredict("Cảm cúm");
        validMedicalExamDto.setSymptomProcess("Sốt, ho, đau họng");
        validMedicalExamDto.setTreatmentProcess("Điều trị triệu chứng");
        validMedicalExamDto.setDurationDay(5);

        // Set follow-up date to 30 days after fixed date
        Calendar followUpCal = Calendar.getInstance();
        followUpCal.setTime(fixedDate);
        followUpCal.add(Calendar.DATE, 30);
        validMedicalExamDto.setFollowUpDate(followUpCal.getTime());

        validMedicalExamDto.setMedicinesExamList(prescriptionItemDtos);
    }

    /**
     * TC_ME_01: Test submit medical examination with valid data
     *
     * Input: Valid MedicalExamDto
     * Expected: HTTP 201 Created
     */
    @Test
    @DisplayName("TC_ME_01: Test submit medical examination with valid data")
    @Rollback(true)
    public void testSubmitMedicalExamination_ValidData() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(testMrl);
        when(medicineService.findById(1)).thenReturn(testMedicine1);
        when(medicineService.findById(2)).thenReturn(testMedicine2);
        when(statusIsApprovedService.findByStatus("PAYMENTPHASE2")).thenReturn(paymentPhase2Status);

        // Act
        ResponseEntity<Object> response = apiBacsiRestController.submitMedicalExamination(validMedicalExamDto);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Thành công !", response.getBody());
    }

    /**
     * TC_ME_02: Test submit medical examination when user is not logged in
     *
     * Input: MedicalExamDto, no current user
     * Expected: HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_ME_02: Test submit medical examination when user is not logged in")
    @Rollback(true)
    public void testSubmitMedicalExamination_NotLoggedIn() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiBacsiRestController.submitMedicalExamination(validMedicalExamDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng không tồn tại", response.getBody());
    }

    /**
     * TC_ME_03: Test submit medical examination with non-existent MRL
     *
     * Input: MedicalExamDto with invalid MRL ID
     * Expected: HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_ME_03: Test submit medical examination with non-existent MRL")
    @Rollback(true)
    public void testSubmitMedicalExamination_NonExistentMRL() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiBacsiRestController.submitMedicalExamination(validMedicalExamDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Không tồn tại đơn hẹn khám cho đơn thuốc này", response.getBody());
    }

    /**
     * TC_ME_04: Test submit medical examination with canceled MRL
     *
     * Input: MedicalExamDto with canceled MRL
     * Expected: HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_ME_04: Test submit medical examination with canceled MRL")
    @Rollback(true)
    public void testSubmitMedicalExamination_CanceledMRL() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);

        // Set MRL as canceled
        testMrl.setIsCanceled(true);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(testMrl);

        // Act
        ResponseEntity<Object> response = apiBacsiRestController.submitMedicalExamination(validMedicalExamDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Phiếu đăng kí này đã bị hủy !", response.getBody());
    }

    /**
     * TC_ME_05: Test submit medical examination with non-existent medicine
     *
     * Input: MedicalExamDto with invalid medicine ID
     * Expected: HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_ME_05: Test submit medical examination with non-existent medicine")
    @Rollback(true)
    public void testSubmitMedicalExamination_NonExistentMedicine() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(testMrl);
        when(medicineService.findById(1)).thenReturn(testMedicine1);
        when(medicineService.findById(2)).thenReturn(null); // Second medicine doesn't exist

        // Act
        ResponseEntity<Object> response = apiBacsiRestController.submitMedicalExamination(validMedicalExamDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Không tồn tại loại thuốc này", response.getBody());
    }

    /**
     * TC_ME_06: Test submit medical examination with existing medical examination
     *
     * Input: MedicalExamDto for MRL that already has a medical examination
     * Expected: HTTP 400 Bad Request
     */
    @Test
    @DisplayName("TC_ME_06: Test submit medical examination with existing medical examination")
    @Rollback(true)
    public void testSubmitMedicalExamination_ExistingMedicalExamination() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);

        // Create existing medical examination
        MedicalExamination existingME = new MedicalExamination();
        existingME.setId(1);
        existingME.setMrl(testMrl);
        testMrl.setMedicalExamination(existingME);

        when(medicalRegistryListService.findById(anyInt())).thenReturn(testMrl);

        // Act
        ResponseEntity<Object> response = apiBacsiRestController.submitMedicalExamination(validMedicalExamDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Phiếu đăng kí này đã có phiếu khám bệnh !", response.getBody());
    }

    /**
     * TC_ME_07: Test submit medical examination with wrong MRL status
     *
     * Input: MedicalExamDto for MRL with wrong status
     * Expected: HTTP 400 Bad Request
     */
    @Test
    @DisplayName("TC_ME_07: Test submit medical examination with wrong MRL status")
    @Rollback(true)
    public void testSubmitMedicalExamination_WrongStatus() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);

        // Set wrong status
        StatusIsApproved wrongStatus = new StatusIsApproved();
        wrongStatus.setId(5);
        wrongStatus.setStatus("CHECKING");
        testMrl.setStatusIsApproved(wrongStatus);

        when(medicalRegistryListService.findById(anyInt())).thenReturn(testMrl);

        // Act
        ResponseEntity<Object> response = apiBacsiRestController.submitMedicalExamination(validMedicalExamDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Phiếu đăng kí này chưa đến lượt khám !", response.getBody());
    }

    /**
     * TC_ME_08: Test submit medical examination without medicines
     *
     * Input: MedicalExamDto without medicines
     * Expected: HTTP 400 Bad Request
     */
    @Test
    @DisplayName("TC_ME_08: Test submit medical examination without medicines")
    @Rollback(true)
    public void testSubmitMedicalExamination_NoMedicines() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(medicalRegistryListService.findById(anyInt())).thenReturn(testMrl);

        // Set empty medicines list
        validMedicalExamDto.setMedicinesExamList(new ArrayList<>());

        // Act
        ResponseEntity<Object> response = apiBacsiRestController.submitMedicalExamination(validMedicalExamDto);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Phiếu khám bệnh phải có ít nhất một loại thuốc !", response.getBody());
    }
}
