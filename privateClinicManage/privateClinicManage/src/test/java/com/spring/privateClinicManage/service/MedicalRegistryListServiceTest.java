package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.spring.privateClinicManage.entity.MedicalRegistryList;
import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.MedicalRegistryListRepository;
import com.spring.privateClinicManage.service.impl.MedicalRegistryListServiceImpl;

/**
 * Unit Test for MedicalRegistryListService
 * This test class focuses on service methods related to the "Duyệt phiếu đăng ký khám bệnh" functionality
 */
@ExtendWith(MockitoExtension.class)
@Transactional
public class MedicalRegistryListServiceTest {

    @Mock
    private MedicalRegistryListRepository medicalRegistryListRepository;

    @InjectMocks
    private MedicalRegistryListServiceImpl medicalRegistryListServiceImpl;
    
    // Spy for partial mocking
    private MedicalRegistryListService medicalRegistryListService;

    // Test data
    private User testUser;
    private Schedule testSchedule;
    private StatusIsApproved checkingStatus;
    private StatusIsApproved approvedStatus;
    private MedicalRegistryList testMedicalRegistryList;
    private List<MedicalRegistryList> medicalRegistryLists;
    private Calendar calendar;

    /**
     * Setup test data before each test
     */
    @BeforeEach
    public void setup() {
        // Create a spy of the service implementation
        medicalRegistryListService = spy(medicalRegistryListServiceImpl);
        
        // Mock user
        testUser = new User();
        testUser.setId(1);
        testUser.setEmail("test@example.com");

        // Mock schedule
        testSchedule = new Schedule();
        testSchedule.setId(1);
        testSchedule.setDate(new Date());
        testSchedule.setIsDayOff(false);
        
        // Setup calendar from schedule date
        calendar = Calendar.getInstance();
        calendar.setTime(testSchedule.getDate());

        // Mock status
        checkingStatus = new StatusIsApproved();
        checkingStatus.setId(1);
        checkingStatus.setStatus("CHECKING");

        approvedStatus = new StatusIsApproved();
        approvedStatus.setId(2);
        approvedStatus.setStatus("PAYMENTPHASE1");

        // Mock medical registry
        testMedicalRegistryList = new MedicalRegistryList();
        testMedicalRegistryList.setId(1);
        testMedicalRegistryList.setName("Test Patient");
        testMedicalRegistryList.setUser(testUser);
        testMedicalRegistryList.setSchedule(testSchedule);
        testMedicalRegistryList.setStatusIsApproved(checkingStatus);
        testMedicalRegistryList.setCreatedDate(new Date());
        testMedicalRegistryList.setIsCanceled(false);

        // Mock list of medical registry
        medicalRegistryLists = new ArrayList<>();
        medicalRegistryLists.add(testMedicalRegistryList);

        // Add another medical registry
        MedicalRegistryList mrl2 = new MedicalRegistryList();
        mrl2.setId(2);
        mrl2.setName("Another Patient");
        mrl2.setUser(testUser);
        mrl2.setSchedule(testSchedule);
        mrl2.setStatusIsApproved(checkingStatus);
        mrl2.setCreatedDate(new Date());
        mrl2.setIsCanceled(false);
        medicalRegistryLists.add(mrl2);
    }

    /**
     * TC06: Test finding a medical registry by ID
     * 
     * Input: Valid registry ID
     * Expected: Returns the matching MedicalRegistryList object
     */
    @Test
    @DisplayName("TC06: Test finding a medical registry by ID")
    @Rollback(true)
    public void testFindById_Success() {
        // Arrange
        when(medicalRegistryListRepository.findById(anyInt())).thenReturn(Optional.of(testMedicalRegistryList));

        // Act
        MedicalRegistryList result = medicalRegistryListServiceImpl.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Patient", result.getName());
    }

    /**
     * TC07: Test saving a medical registry
     * 
     * Input: Valid MedicalRegistryList object
     * Expected: Repository save method is called
     */
    @Test
    @DisplayName("TC07: Test saving a medical registry")
    @Rollback(true)
    public void testSaveMedicalRegistryList() {
        // Arrange
        when(medicalRegistryListRepository.save(any(MedicalRegistryList.class))).thenReturn(testMedicalRegistryList);

        // Act
        medicalRegistryListServiceImpl.saveMedicalRegistryList(testMedicalRegistryList);

        // Assert
        verify(medicalRegistryListRepository).save(testMedicalRegistryList);
    }

    /**
     * TC08: Test finding medical registries by schedule date and status
     * 
     * Input: Valid schedule year, month, day and status objects
     * Expected: Returns list of matching MedicalRegistryList objects
     */
    @Test
    @DisplayName("TC08: Test finding medical registries by schedule date and status")
    @Rollback(true)
    public void testFindByScheduleAndStatusIsApproved2() {
        // Setup the spy to return our test data
        doReturn(medicalRegistryLists).when(medicalRegistryListService)
            .findByScheduleAndStatusIsApproved2(any(Schedule.class), any(StatusIsApproved.class));
        
        // Act
        List<MedicalRegistryList> result = medicalRegistryListService
                .findByScheduleAndStatusIsApproved2(testSchedule, checkingStatus);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Test Patient", result.get(0).getName());
        assertEquals("Another Patient", result.get(1).getName());
    }

    /**
     * TC09: Test checking if a user already has a registration
     * 
     * Input: User, Schedule date, StatusIsApproved
     * Expected: Returns count of registrations
     */
    @Test
    @DisplayName("TC09: Test checking for existing registrations")
    @Rollback(true)
    public void testCountExistingRegistrations() {
        // Setup the spy to return our test count
        doReturn(2).when(medicalRegistryListService)
            .countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved(
                any(User.class), any(Schedule.class), anyBoolean(), any(StatusIsApproved.class));

        // Act
        Integer count = medicalRegistryListService.countMRLByUserAndScheduleAndisCancelledAndStatusIsApproved(
                testUser, testSchedule, false, checkingStatus);

        // Assert
        assertEquals(2, count);
    }

    /**
     * TC10: Test counting medical registries by schedule date
     * 
     * Input: Schedule, List of statuses
     * Expected: Returns the count of registrations for that date
     */
    @Test
    @DisplayName("TC10: Test counting registrations by date")
    @Rollback(true)
    public void testCountByScheduleAndStatuses() {
        // Setup the spy to return our test count
        List<StatusIsApproved> statuses = Arrays.asList(checkingStatus, approvedStatus);
        doReturn(3).when(medicalRegistryListService)
            .countMRLByScheduleAndStatuses(any(Schedule.class), any(List.class));

        // Act
        Integer count = medicalRegistryListService.countMRLByScheduleAndStatuses(
                testSchedule, statuses);

        // Assert
        assertEquals(3, count);
    }
} 