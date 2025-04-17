package com.spring.privateClinicManage.component;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.annotation.Rollback;

import com.spring.privateClinicManage.dto.OnlineUsersOutputDto;
import com.spring.privateClinicManage.entity.Role;
import com.spring.privateClinicManage.entity.User;

/**
 * Unit tests for OnlinerUsers
 * 
 * This test class covers the main functionality of the OnlinerUsers utility,
 * which is used to manage online users in the chat feature.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class OnlinerUsersTest {

    private OnlinerUsers onlinerUsers;
    
    // Test data
    private User patient;
    private User consultant;
    private User doctor;
    private Role patientRole;
    private Role consultantRole;
    private Role doctorRole;
    private Map<String, List<OnlineUsersOutputDto>> onlineUsersMap;
    
    /**
     * Setup test data before each test
     */
    @BeforeEach
    public void setup() {
        onlinerUsers = new OnlinerUsers();
        
        // Mock user roles
        patientRole = new Role();
        patientRole.setId(1);
        patientRole.setName("ROLE_BENHNHAN");
        
        consultantRole = new Role();
        consultantRole.setId(2);
        consultantRole.setName("ROLE_TUVAN");
        
        doctorRole = new Role();
        doctorRole.setId(3);
        doctorRole.setName("ROLE_BACSI");
        
        // Mock users
        patient = new User();
        patient.setId(1);
        patient.setEmail("patient@example.com");
        patient.setName("Patient User");
        patient.setRole(patientRole);
        
        consultant = new User();
        consultant.setId(2);
        consultant.setEmail("consultant@example.com");
        consultant.setName("Consultant User");
        consultant.setRole(consultantRole);
        
        doctor = new User();
        doctor.setId(3);
        doctor.setEmail("doctor@example.com");
        doctor.setName("Doctor User");
        doctor.setRole(doctorRole);
        
        // Initialize online users map
        onlineUsersMap = new HashMap<>();
        
        // Add patient to online users
        List<OnlineUsersOutputDto> patientList = new ArrayList<>();
        patientList.add(new OnlineUsersOutputDto(patient, "session1"));
        onlineUsersMap.put("ROLE_BENHNHAN", patientList);
        
        // Add consultant to online users
        List<OnlineUsersOutputDto> consultantList = new ArrayList<>();
        consultantList.add(new OnlineUsersOutputDto(consultant, "session2"));
        onlineUsersMap.put("ROLE_TUVAN", consultantList);
        
        // Set online users map
        onlinerUsers.setOnlineUsers(onlineUsersMap);
    }
    
    /**
     * TC_OU_01: Test getting online users
     * 
     * Input: None
     * Expected: Returns map of online users
     */
    @Test
    @DisplayName("TC_OU_01: Test getting online users")
    @Rollback(true)
    public void testGetOnlineUsers() {
        // Act
        Map<String, List<OnlineUsersOutputDto>> result = onlinerUsers.getOnlineUsers();
        
        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("ROLE_BENHNHAN"));
        assertTrue(result.containsKey("ROLE_TUVAN"));
        assertEquals(1, result.get("ROLE_BENHNHAN").size());
        assertEquals(1, result.get("ROLE_TUVAN").size());
        assertEquals(patient, result.get("ROLE_BENHNHAN").get(0).getUser());
        assertEquals(consultant, result.get("ROLE_TUVAN").get(0).getUser());
    }
    
    /**
     * TC_OU_02: Test finding first consultant
     * 
     * Input: None
     * Expected: Returns first consultant user
     */
    @Test
    @DisplayName("TC_OU_02: Test finding first consultant")
    @Rollback(true)
    public void testFindFirstROLE_TUVAN() {
        // Act
        User result = onlinerUsers.findFirstROLE_TUVAN();
        
        // Assert
        assertNotNull(result);
        assertEquals(consultant, result);
    }
    
    /**
     * TC_OU_03: Test finding first consultant when no consultant is online
     * 
     * Input: None (no consultant online)
     * Expected: Returns null
     */
    @Test
    @DisplayName("TC_OU_03: Test finding first consultant when no consultant is online")
    @Rollback(true)
    public void testFindFirstROLE_TUVAN_NoConsultantOnline() {
        // Arrange
        onlineUsersMap.remove("ROLE_TUVAN");
        
        // Act
        User result = onlinerUsers.findFirstROLE_TUVAN();
        
        // Assert
        assertNull(result);
    }
    
    /**
     * TC_OU_04: Test checking if user is online
     * 
     * Input: User that is online
     * Expected: Returns true
     */
    @Test
    @DisplayName("TC_OU_04: Test checking if user is online")
    @Rollback(true)
    public void testIsUserOnline_UserIsOnline() {
        // Act
        Boolean result = onlinerUsers.isUserOnline(patient);
        
        // Assert
        assertTrue(result);
    }
    
    /**
     * TC_OU_05: Test checking if user is online when user is not online
     * 
     * Input: User that is not online
     * Expected: Returns false
     */
    @Test
    @DisplayName("TC_OU_05: Test checking if user is online when user is not online")
    @Rollback(true)
    public void testIsUserOnline_UserIsNotOnline() {
        // Act
        Boolean result = onlinerUsers.isUserOnline(doctor);
        
        // Assert
        assertFalse(result);
    }
    
    /**
     * TC_OU_06: Test checking if user is online when role has no online users
     * 
     * Input: User with role that has no online users
     * Expected: Returns false
     */
    @Test
    @DisplayName("TC_OU_06: Test checking if user is online when role has no online users")
    @Rollback(true)
    public void testIsUserOnline_RoleHasNoOnlineUsers() {
        // Arrange
        onlineUsersMap.remove("ROLE_BACSI");
        
        // Act
        Boolean result = onlinerUsers.isUserOnline(doctor);
        
        // Assert
        assertFalse(result);
    }
    
    /**
     * TC_OU_07: Test finding and removing session ID by key
     * 
     * Input: Valid role name, session ID, and user ID
     * Expected: Session is removed from online users
     */
    @Test
    @DisplayName("TC_OU_07: Test finding and removing session ID by key")
    @Rollback(true)
    public void testFindAndRemoveSessionIdByKey() {
        // Act
        onlinerUsers.findAndRemoveSessionIdByKey("ROLE_BENHNHAN", "session1", 1);
        
        // Assert
        Map<String, List<OnlineUsersOutputDto>> result = onlinerUsers.getOnlineUsers();
        assertTrue(result.containsKey("ROLE_BENHNHAN"));
        assertEquals(0, result.get("ROLE_BENHNHAN").size());
    }
    
    /**
     * TC_OU_08: Test finding and removing session ID by key when role has no online users
     * 
     * Input: Role name with no online users, session ID, and user ID
     * Expected: No exception is thrown
     */
    @Test
    @DisplayName("TC_OU_08: Test finding and removing session ID by key when role has no online users")
    @Rollback(true)
    public void testFindAndRemoveSessionIdByKey_RoleHasNoOnlineUsers() {
        // Arrange
        onlineUsersMap.remove("ROLE_BACSI");
        
        // Act & Assert
        assertDoesNotThrow(() -> onlinerUsers.findAndRemoveSessionIdByKey("ROLE_BACSI", "session3", 3));
    }
    
    /**
     * TC_OU_09: Test finding and removing session ID by key when user is not online
     * 
     * Input: Valid role name, session ID, and user ID that is not online
     * Expected: No exception is thrown
     */
    @Test
    @DisplayName("TC_OU_09: Test finding and removing session ID by key when user is not online")
    @Rollback(true)
    public void testFindAndRemoveSessionIdByKey_UserIsNotOnline() {
        // Act & Assert
        assertDoesNotThrow(() -> onlinerUsers.findAndRemoveSessionIdByKey("ROLE_BENHNHAN", "session999", 999));
        
        // Verify that the original user is still online
        Map<String, List<OnlineUsersOutputDto>> result = onlinerUsers.getOnlineUsers();
        assertTrue(result.containsKey("ROLE_BENHNHAN"));
        assertEquals(1, result.get("ROLE_BENHNHAN").size());
        assertEquals(patient, result.get("ROLE_BENHNHAN").get(0).getUser());
    }
}
