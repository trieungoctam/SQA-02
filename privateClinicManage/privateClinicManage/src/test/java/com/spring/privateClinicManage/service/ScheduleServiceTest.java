package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;

import com.spring.privateClinicManage.entity.Schedule;
import com.spring.privateClinicManage.repository.ScheduleRepository;
import com.spring.privateClinicManage.service.impl.ScheduleServiceImpl;

/**
 * Unit tests for ScheduleService
 *
 * This test class covers the main functionality of the ScheduleService,
 * which is used to manage clinic schedules.
 */
@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    @Mock
    private ScheduleRepository scheduleRepository;

    @InjectMocks
    private ScheduleServiceImpl scheduleService;

    // Test data
    private Schedule testSchedule;
    private List<Schedule> scheduleList;
    private Date testDate;

    /**
     * Setup test data before each test
     */
    @BeforeEach
    public void setup() {
        // Create test date
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.JULY, 15); // July 15, 2023
        testDate = calendar.getTime();

        // Create test schedule
        testSchedule = new Schedule();
        testSchedule.setId(1);
        testSchedule.setDate(testDate);
        testSchedule.setIsDayOff(false);
        testSchedule.setDescription("Normal working day");

        // Create schedule list
        scheduleList = new ArrayList<>();
        scheduleList.add(testSchedule);

        // Add more schedules to the list
        for (int i = 2; i <= 5; i++) {
            Schedule schedule = new Schedule();
            schedule.setId(i);

            // Set date to consecutive days
            Calendar cal = Calendar.getInstance();
            cal.set(2023, Calendar.JULY, 15 + i - 1);
            schedule.setDate(cal.getTime());

            schedule.setIsDayOff(i % 2 == 0); // Even IDs are day off
            schedule.setDescription(i % 2 == 0 ? "Day off" : "Normal working day");

            scheduleList.add(schedule);
        }
    }

    /**
     * TC_SCH_01: Test finding schedule by date
     *
     * Input: Valid date
     * Expected: Returns the matching schedule
     */
    @Test
    @DisplayName("TC_SCH_01: Test finding schedule by date")
    @Rollback(true)
    public void testFindByDate() {
        // Arrange
        when(scheduleRepository.findByDate(testDate)).thenReturn(testSchedule);

        // Act
        Schedule result = scheduleService.findByDate(testDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(testDate, result.getDate());
        assertFalse(result.getIsDayOff());

        // Verify repository call
        verify(scheduleRepository).findByDate(testDate);
    }

    /**
     * TC_SCH_02: Test saving a new schedule
     *
     * Input: Valid schedule object
     * Expected: Schedule is saved successfully
     */
    @Test
    @DisplayName("TC_SCH_02: Test saving a new schedule")
    @Rollback(true)
    public void testSaveSchedule() {
        // Arrange
        Schedule newSchedule = new Schedule();
        newSchedule.setDate(new Date());
        newSchedule.setIsDayOff(false);
        newSchedule.setDescription("New schedule");

        // Act
        scheduleService.saveSchedule(newSchedule);

        // Assert & Verify
        verify(scheduleRepository).save(newSchedule);
    }

    /**
     * TC_SCH_03: Test finding schedule by day, month, year
     *
     * Input: Valid day, month, year
     * Expected: Returns the matching schedule
     */
    @Test
    @DisplayName("TC_SCH_03: Test finding schedule by day, month, year")
    @Rollback(true)
    public void testFindByDayMonthYear() {
        // Arrange
        when(scheduleRepository.findByDayMonthYear(2023, 7, 15)).thenReturn(testSchedule);

        // Act
        Schedule result = scheduleService.findByDayMonthYear(2023, 7, 15);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertFalse(result.getIsDayOff());

        // Verify repository call
        verify(scheduleRepository).findByDayMonthYear(2023, 7, 15);
    }

    /**
     * TC_SCH_04: Test finding all schedules
     *
     * Input: None
     * Expected: Returns list of all schedules
     */
    @Test
    @DisplayName("TC_SCH_04: Test finding all schedules")
    @Rollback(true)
    public void testFindAllSchedule() {
        // Arrange
        when(scheduleRepository.findAll()).thenReturn(scheduleList);

        // Act
        List<Schedule> result = scheduleService.findAllSchedule();

        // Assert
        assertNotNull(result);
        assertEquals(5, result.size());

        // Verify repository call
        verify(scheduleRepository).findAll();
    }

    /**
     * TC_SCH_05: Test schedule pagination
     *
     * Input: Page number, size, and schedule list
     * Expected: Returns paginated schedule list
     */
    @Test
    @DisplayName("TC_SCH_05: Test schedule pagination")
    @Rollback(true)
    public void testSchedulePaginated() {
        // Arrange
        int page = 1;
        int size = 2;

        // Act
        Page<Schedule> result = scheduleService.schedulePaginated(page, size, scheduleList);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(5, result.getTotalElements());
        assertEquals(3, result.getTotalPages());
    }

    /**
     * TC_SCH_06: Test finding schedule by ID
     *
     * Input: Valid schedule ID
     * Expected: Returns the matching schedule
     */
    @Test
    @DisplayName("TC_SCH_06: Test finding schedule by ID")
    @Rollback(true)
    public void testFindById() {
        // Arrange
        when(scheduleRepository.findById(1)).thenReturn(Optional.of(testSchedule));

        // Act
        Schedule result = scheduleService.findById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(testDate, result.getDate());

        // Verify repository call
        verify(scheduleRepository).findById(1);
    }

    /**
     * TC_SCH_07: Test finding schedule by ID when not found
     *
     * Input: Non-existent schedule ID
     * Expected: Returns null
     */
    @Test
    @DisplayName("TC_SCH_07: Test finding schedule by ID when not found")
    @Rollback(true)
    public void testFindById_NotFound() {
        // Arrange
        when(scheduleRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        Schedule result = scheduleService.findById(999);

        // Assert
        assertNull(result);

        // Verify repository call
        verify(scheduleRepository).findById(999);
    }
}
