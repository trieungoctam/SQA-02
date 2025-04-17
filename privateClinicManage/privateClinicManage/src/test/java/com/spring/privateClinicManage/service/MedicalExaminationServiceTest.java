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

import com.spring.privateClinicManage.entity.MedicalExamination;
import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.Role;
import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.MedicalExaminationRepository;
import com.spring.privateClinicManage.service.impl.MedicalExaminationServiceImpl;

/**
 * Unit tests for MedicalExaminationService
 * 
 * This test class covers the main functionality of the MedicalExaminationService,
 * which is used to manage medical examinations.
 */
@ExtendWith(MockitoExtension.class)
public class MedicalExaminationServiceTest {

    @Mock
    private MedicalExaminationRepository medicalExaminationRepository;
    
    @InjectMocks
    private MedicalExaminationServiceImpl medicalExaminationService;
    
    // Test data
    private MedicalExamination testMedicalExamination;
    private MedicalRegistryList testMrl;
    private User testDoctor;
    private User testPatient;
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
        testDoctor = new User();
        testDoctor.setId(1);
        testDoctor.setEmail("bacsi@example.com");
        testDoctor.setRole(bacsiRole);
        
        testPatient = new User();
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
        testMrl = new MedicalRegistryList();
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
        
        // Set follow-up date to 30 days after fixed date
        Calendar followUpCal = Calendar.getInstance();
        followUpCal.setTime(fixedDate);
        followUpCal.add(Calendar.DATE, 30);
        testMedicalExamination.setFollowUpDate(followUpCal.getTime());
        
        testMedicalExamination.setUserCreated(testDoctor);
        testMedicalExamination.setMrl(testMrl);
    }
    
    /**
     * TC_MES_01: Test saving a medical examination
     * 
     * Input: Valid MedicalExamination object
     * Expected: MedicalExamination is saved successfully
     */
    @Test
    @DisplayName("TC_MES_01: Test saving a medical examination")
    @Rollback(true)
    public void testSaveMedicalExamination() {
        // Arrange
        when(medicalExaminationRepository.save(any(MedicalExamination.class))).thenReturn(testMedicalExamination);
        
        // Act
        medicalExaminationService.saveMedicalExamination(testMedicalExamination);
        
        // Assert
        verify(medicalExaminationRepository).save(testMedicalExamination);
    }
    
    /**
     * TC_MES_02: Test finding medical examination by MRL
     * 
     * Input: Valid MedicalRegistryList
     * Expected: Returns the matching MedicalExamination
     */
    @Test
    @DisplayName("TC_MES_02: Test finding medical examination by MRL")
    @Rollback(true)
    public void testFindByMrl() {
        // Arrange
        when(medicalExaminationRepository.findByMrl(testMrl)).thenReturn(testMedicalExamination);
        
        // Act
        MedicalExamination result = medicalExaminationService.findByMrl(testMrl);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Cảm cúm", result.getPredict());
        assertEquals(testDoctor, result.getUserCreated());
        assertEquals(testMrl, result.getMrl());
    }
    
    /**
     * TC_MES_03: Test finding medical examination by ID
     * 
     * Input: Valid medical examination ID
     * Expected: Returns the matching MedicalExamination
     */
    @Test
    @DisplayName("TC_MES_03: Test finding medical examination by ID")
    @Rollback(true)
    public void testFindById() {
        // Arrange
        when(medicalExaminationRepository.findById(1)).thenReturn(Optional.of(testMedicalExamination));
        
        // Act
        MedicalExamination result = medicalExaminationService.findById(1);
        
        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Cảm cúm", result.getPredict());
        assertEquals(testDoctor, result.getUserCreated());
        assertEquals(testMrl, result.getMrl());
    }
    
    /**
     * TC_MES_04: Test finding medical examination by ID when not found
     * 
     * Input: Non-existent medical examination ID
     * Expected: Returns null
     */
    @Test
    @DisplayName("TC_MES_04: Test finding medical examination by ID when not found")
    @Rollback(true)
    public void testFindById_NotFound() {
        // Arrange
        when(medicalExaminationRepository.findById(999)).thenReturn(Optional.empty());
        
        // Act
        MedicalExamination result = medicalExaminationService.findById(999);
        
        // Assert
        assertNull(result);
    }
    
    /**
     * TC_MES_05: Test finding medical examination by MRL when not found
     * 
     * Input: MRL with no associated medical examination
     * Expected: Returns null
     */
    @Test
    @DisplayName("TC_MES_05: Test finding medical examination by MRL when not found")
    @Rollback(true)
    public void testFindByMrl_NotFound() {
        // Arrange
        MedicalRegistryList newMrl = new MedicalRegistryList();
        newMrl.setId(2);
        when(medicalExaminationRepository.findByMrl(newMrl)).thenReturn(null);
        
        // Act
        MedicalExamination result = medicalExaminationService.findByMrl(newMrl);
        
        // Assert
        assertNull(result);
    }
}
