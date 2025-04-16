package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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

import com.spring.privateClinicManage.entity.StatusIsApproved;
import com.spring.privateClinicManage.repository.StatusIsApprovedRepository;
import com.spring.privateClinicManage.service.impl.StatusIsApprovedServiceImpl;

/**
 * Unit Test for StatusIsApprovedService
 * This test class focuses on status handling for the "Duyệt phiếu đăng ký khám bệnh" functionality
 */
@ExtendWith(MockitoExtension.class)
@Transactional
public class StatusIsApprovedServiceTest {

    @Mock
    private StatusIsApprovedRepository statusIsApprovedRepository;

    @InjectMocks
    private StatusIsApprovedServiceImpl statusIsApprovedService;

    // Test data
    private StatusIsApproved checkingStatus;
    private StatusIsApproved approvedStatus;
    private StatusIsApproved failedStatus;
    private List<StatusIsApproved> allStatuses;

    /**
     * Setup test data before each test
     */
    @BeforeEach
    public void setup() {
        // Create status objects
        checkingStatus = new StatusIsApproved();
        checkingStatus.setId(1);
        checkingStatus.setStatus("CHECKING");
        checkingStatus.setNote("Đang chờ phê duyệt");

        approvedStatus = new StatusIsApproved();
        approvedStatus.setId(2);
        approvedStatus.setStatus("PAYMENTPHASE1");
        approvedStatus.setNote("Đã phê duyệt, chờ thanh toán");

        failedStatus = new StatusIsApproved();
        failedStatus.setId(3);
        failedStatus.setStatus("FAILED");
        failedStatus.setNote("Từ chối đơn đăng ký");

        // Create list of all statuses
        allStatuses = new ArrayList<>();
        allStatuses.add(checkingStatus);
        allStatuses.add(approvedStatus);
        allStatuses.add(failedStatus);
    }

    /**
     * TC11: Test finding status by name - valid case
     * 
     * Input: Valid status name
     * Expected: Returns the matching StatusIsApproved object
     */
    @Test
    @DisplayName("TC11: Test finding status by name - valid case")
    @Rollback(true)
    public void testFindByStatus_ValidStatus() {
        // Arrange
        when(statusIsApprovedRepository.findByStatus("CHECKING")).thenReturn(checkingStatus);

        // Act
        StatusIsApproved result = statusIsApprovedService.findByStatus("CHECKING");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("CHECKING", result.getStatus());
        assertEquals("Đang chờ phê duyệt", result.getNote());
    }

    /**
     * TC12: Test finding status by name - invalid case
     * 
     * Input: Invalid status name
     * Expected: Returns null
     */
    @Test
    @DisplayName("TC12: Test finding status by name - invalid case")
    @Rollback(true)
    public void testFindByStatus_InvalidStatus() {
        // Arrange
        when(statusIsApprovedRepository.findByStatus(anyString())).thenReturn(null);

        // Act
        StatusIsApproved result = statusIsApprovedService.findByStatus("NON_EXISTENT_STATUS");

        // Assert
        assertNull(result);
    }

    /**
     * TC13: Test availability of different status types
     * 
     * Input: Different status names
     * Expected: Each status type exists and returns the correct object
     */
    @Test
    @DisplayName("TC13: Test availability of different status types")
    @Rollback(true)
    public void testAvailabilityOfDifferentStatusTypes() {
        // Arrange
        when(statusIsApprovedRepository.findByStatus("CHECKING")).thenReturn(checkingStatus);
        when(statusIsApprovedRepository.findByStatus("PAYMENTPHASE1")).thenReturn(approvedStatus);
        when(statusIsApprovedRepository.findByStatus("FAILED")).thenReturn(failedStatus);

        // Act
        StatusIsApproved checkResult = statusIsApprovedService.findByStatus("CHECKING");
        StatusIsApproved approveResult = statusIsApprovedService.findByStatus("PAYMENTPHASE1");
        StatusIsApproved failResult = statusIsApprovedService.findByStatus("FAILED");

        // Assert
        assertNotNull(checkResult);
        assertEquals("CHECKING", checkResult.getStatus());
        
        assertNotNull(approveResult);
        assertEquals("PAYMENTPHASE1", approveResult.getStatus());
        
        assertNotNull(failResult);
        assertEquals("FAILED", failResult.getStatus());
    }
} 