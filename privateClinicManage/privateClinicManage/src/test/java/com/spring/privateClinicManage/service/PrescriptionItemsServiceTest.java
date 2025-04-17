package com.spring.privateClinicManage.service;

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
import org.springframework.test.annotation.Rollback;

import com.spring.privateClinicManage.entity.Medicine;
import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.PrescriptionItems;
import com.spring.privateClinicManage.entity.Role;
import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.PrescriptionItemsRepository;
import com.spring.privateClinicManage.service.impl.PrescriptionItemsServiceImpl;

/**
 * Unit tests for PrescriptionItemsService
 *
 * This test class covers the main functionality of the PrescriptionItemsService,
 * which is used to manage prescription items.
 */
@ExtendWith(MockitoExtension.class)
public class PrescriptionItemsServiceTest {

    @Mock
    private PrescriptionItemsRepository prescriptionItemsRepository;

    @InjectMocks
    private PrescriptionItemsServiceImpl prescriptionItemsService;

    // Test data
    private PrescriptionItems testPrescriptionItem;
    private MedicalExamination testMedicalExamination;
    private Medicine testMedicine;
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
        User testDoctor = new User();
        testDoctor.setId(1);
        testDoctor.setEmail("bacsi@example.com");
        testDoctor.setRole(bacsiRole);

        User testPatient = new User();
        testPatient.setId(2);
        testPatient.setEmail("patient@example.com");
        testPatient.setRole(benhnhanRole);

        // Mock schedule
        Schedule testSchedule = new Schedule();
        testSchedule.setId(1);
        testSchedule.setDate(fixedDate);
        testSchedule.setIsDayOff(false);

        // Mock status
        StatusIsApproved processingStatus = new StatusIsApproved();
        processingStatus.setId(3);
        processingStatus.setStatus("PROCESSING");

        // Mock MedicalRegistryList
        MedicalRegistryList testMrl = new MedicalRegistryList();
        testMrl.setId(1);
        testMrl.setName("Test Patient");
        testMrl.setFavor("Test symptoms");
        testMrl.setCreatedDate(fixedDate);
        testMrl.setIsCanceled(false);
        testMrl.setUser(testPatient);
        testMrl.setSchedule(testSchedule);
        testMrl.setStatusIsApproved(processingStatus);

        // Mock MedicalExamination
        testMedicalExamination = new MedicalExamination();
        testMedicalExamination.setId(1);
        testMedicalExamination.setCreatedDate(fixedDate);
        testMedicalExamination.setPredict("Cảm cúm");
        testMedicalExamination.setAdvance("Nghỉ ngơi, uống nhiều nước");
        testMedicalExamination.setSymptomProcess("Sốt, ho, đau họng");
        testMedicalExamination.setTreatmentProcess("Điều trị triệu chứng");
        testMedicalExamination.setDurationDay(5);
        testMedicalExamination.setUserCreated(testDoctor);
        testMedicalExamination.setMrl(testMrl);

        // Mock Medicine
        testMedicine = new Medicine();
        testMedicine.setId(1);
        testMedicine.setName("Paracetamol");
        testMedicine.setPrice(10000L);

        // Mock PrescriptionItems
        testPrescriptionItem = new PrescriptionItems();
        testPrescriptionItem.setId(1);
        testPrescriptionItem.setUsage("Uống sau ăn");
        testPrescriptionItem.setPrognosis(3); // 3 viên mỗi ngày
        testPrescriptionItem.setMedicine(testMedicine);
        testPrescriptionItem.setMedicalExamination(testMedicalExamination);
    }

    /**
     * TC_PIS_01: Test saving a prescription item
     *
     * Input: Valid PrescriptionItems object
     * Expected: PrescriptionItems is saved successfully
     */
    @Test
    @DisplayName("TC_PIS_01: Test saving a prescription item")
    @Rollback(true)
    public void testSavePrescriptionItems() {
        // Arrange
        when(prescriptionItemsRepository.save(any(PrescriptionItems.class))).thenReturn(testPrescriptionItem);

        // Act
        prescriptionItemsService.savePrescriptionItems(testPrescriptionItem);

        // Assert
        verify(prescriptionItemsRepository).save(testPrescriptionItem);
    }

    /**
     * TC_PIS_02: Test finding prescription items by medical examination
     *
     * Input: Valid MedicalExamination
     * Expected: Returns list of matching PrescriptionItems
     */
    @Test
    @DisplayName("TC_PIS_02: Test finding prescription items by medical examination")
    @Rollback(true)
    public void testFindByMedicalExamination() {
        // Arrange
        List<PrescriptionItems> prescriptionItems = new ArrayList<>();
        prescriptionItems.add(testPrescriptionItem);

        when(prescriptionItemsRepository.findByMedicalExamination(testMedicalExamination)).thenReturn(prescriptionItems);

        // Act
        List<PrescriptionItems> result = prescriptionItemsService.findByMedicalExamination(testMedicalExamination);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals("Uống sau ăn", result.get(0).getUsage());
        assertEquals(3, result.get(0).getPrognosis());
        assertEquals(testMedicine, result.get(0).getMedicine());
        assertEquals(testMedicalExamination, result.get(0).getMedicalExamination());
    }

    /**
     * TC_PIS_03: Test finding prescription items by medical examination when none exist
     *
     * Input: MedicalExamination with no prescription items
     * Expected: Returns empty list
     */
    @Test
    @DisplayName("TC_PIS_03: Test finding prescription items by medical examination when none exist")
    @Rollback(true)
    public void testFindByMedicalExamination_Empty() {
        // Arrange
        MedicalExamination newMedicalExamination = new MedicalExamination();
        newMedicalExamination.setId(2);

        when(prescriptionItemsRepository.findByMedicalExamination(newMedicalExamination)).thenReturn(new ArrayList<>());

        // Act
        List<PrescriptionItems> result = prescriptionItemsService.findByMedicalExamination(newMedicalExamination);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    /**
     * TC_PIS_04: Test finding prescription item by ID
     *
     * Input: Valid prescription item ID
     * Expected: Returns the matching PrescriptionItems
     */
    @Test
    @DisplayName("TC_PIS_04: Test finding prescription item by ID")
    @Rollback(true)
    public void testFindById() {
        // Arrange
        when(prescriptionItemsRepository.findById(1)).thenReturn(Optional.of(testPrescriptionItem));

        // Act
        PrescriptionItems result = prescriptionItemsService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Uống sau ăn", result.getUsage());
        assertEquals(3, result.getPrognosis());
        assertEquals(testMedicine, result.getMedicine());
        assertEquals(testMedicalExamination, result.getMedicalExamination());
    }

    /**
     * TC_PIS_05: Test finding prescription item by ID when not found
     *
     * Input: Non-existent prescription item ID
     * Expected: Returns null
     */
    @Test
    @DisplayName("TC_PIS_05: Test finding prescription item by ID when not found")
    @Rollback(true)
    public void testFindById_NotFound() {
        // Arrange
        when(prescriptionItemsRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        PrescriptionItems result = prescriptionItemsService.findById(999);

        // Assert
        assertNull(result);
    }
}
