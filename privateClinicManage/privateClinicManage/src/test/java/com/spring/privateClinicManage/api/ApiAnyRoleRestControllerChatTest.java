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
import org.springframework.test.annotation.Rollback;

import com.spring.privateClinicManage.dto.GetChatMessageDto;
import com.spring.privateClinicManage.dto.RecipientDto;
import com.spring.privateClinicManage.entity.ChatMessage;
import com.spring.privateClinicManage.entity.ChatRoom;
import com.spring.privateClinicManage.entity.Role;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.service.ChatMessageService;
import com.spring.privateClinicManage.service.ChatRoomService;
import com.spring.privateClinicManage.service.UserService;
import com.spring.privateClinicManage.component.OnlinerUsers;

/**
 * Unit tests for ApiAnyRoleRestController - Chat functionality
 *
 * This test class covers the chat-related functionality of the ApiAnyRoleRestController,
 * which is used to handle REST API requests for the chat feature.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ApiAnyRoleRestControllerChatTest {

    @Mock
    private UserService userService;

    @Mock
    private ChatMessageService chatMessageService;

    @Mock
    private ChatRoomService chatRoomService;

    @Mock
    private OnlinerUsers onlineUsers;

    @InjectMocks
    private ApiAnyRoleRestController apiAnyRoleRestController;

    // Test data
    private User currentUser;
    private User consultant;
    private ChatMessage chatMessage;
    private List<ChatMessage> chatMessages;
    private GetChatMessageDto getChatMessageDto;
    private String chatRoomId;
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
        Role benhnhanRole = new Role();
        benhnhanRole.setId(1);
        benhnhanRole.setName("ROLE_BENHNHAN");

        Role tuvanRole = new Role();
        tuvanRole.setId(2);
        tuvanRole.setName("ROLE_TUVAN");

        // Mock users
        currentUser = new User();
        currentUser.setId(1);
        currentUser.setEmail("patient@example.com");
        currentUser.setName("Patient User");
        currentUser.setRole(benhnhanRole);

        consultant = new User();
        consultant.setId(2);
        consultant.setEmail("consultant@example.com");
        consultant.setName("Consultant User");
        consultant.setRole(tuvanRole);

        // Create chat room ID
        chatRoomId = "1_2";

        // Mock ChatMessage
        chatMessage = new ChatMessage();
        chatMessage.setId(1);
        chatMessage.setSender(currentUser);
        chatMessage.setRecipient(consultant);
        chatMessage.setChatRoomId(chatRoomId);
        chatMessage.setContent("Hello, this is a test message");
        chatMessage.setCreatedDate(fixedDate);

        // Create list of chat messages
        chatMessages = new ArrayList<>();
        chatMessages.add(chatMessage);

        // Mock GetChatMessageDto
        getChatMessageDto = new GetChatMessageDto();
        getChatMessageDto.setSenderId(1);
        getChatMessageDto.setRecipientId(2);
    }

    /**
     * TC_AARC_01: Test getting all chat messages by sender and recipient
     *
     * Input: Valid GetChatMessageDto
     * Expected: Returns list of chat messages
     */
    @Test
    @DisplayName("TC_AARC_01: Test getting all chat messages by sender and recipient")
    @Rollback(true)
    public void testGetAllChatMessageBySenderAndRecipient() {
        // Arrange
        when(userService.findUserById(1)).thenReturn(currentUser);
        when(userService.findUserById(2)).thenReturn(consultant);
        when(chatMessageService.findBySenderAndRecipient(currentUser, consultant)).thenReturn(chatMessages);

        // Act
        ResponseEntity<Object> response = apiAnyRoleRestController.getAllChatMessageBySenderAndRecipient(getChatMessageDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(chatMessages, response.getBody());
    }

    /**
     * TC_AARC_02: Test getting all chat messages when sender doesn't exist
     *
     * Input: GetChatMessageDto with invalid sender ID
     * Expected: Returns HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_AARC_02: Test getting all chat messages when sender doesn't exist")
    @Rollback(true)
    public void testGetAllChatMessageBySenderAndRecipient_SenderNotFound() {
        // Arrange
        when(userService.findUserById(1)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiAnyRoleRestController.getAllChatMessageBySenderAndRecipient(getChatMessageDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người gửi không tồn tại", response.getBody());
    }

    /**
     * TC_AARC_03: Test getting all chat messages when recipient doesn't exist
     *
     * Input: GetChatMessageDto with invalid recipient ID
     * Expected: Returns HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_AARC_03: Test getting all chat messages when recipient doesn't exist")
    @Rollback(true)
    public void testGetAllChatMessageBySenderAndRecipient_RecipientNotFound() {
        // Arrange
        when(userService.findUserById(1)).thenReturn(currentUser);
        when(userService.findUserById(2)).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiAnyRoleRestController.getAllChatMessageBySenderAndRecipient(getChatMessageDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người nhận không tồn tại", response.getBody());
    }

    /**
     * TC_AARC_04: Test getting all chat messages when sender ID is null
     *
     * Input: GetChatMessageDto with null sender ID
     * Expected: Returns HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_AARC_04: Test getting all chat messages when sender ID is null")
    @Rollback(true)
    public void testGetAllChatMessageBySenderAndRecipient_SenderIdNull() {
        // Arrange
        getChatMessageDto.setSenderId(null);

        // Act
        ResponseEntity<Object> response = apiAnyRoleRestController.getAllChatMessageBySenderAndRecipient(getChatMessageDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người gửi không tồn tại", response.getBody());
    }

    /**
     * TC_AARC_05: Test getting all chat messages when recipient ID is null
     *
     * Input: GetChatMessageDto with null recipient ID
     * Expected: Returns HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_AARC_05: Test getting all chat messages when recipient ID is null")
    @Rollback(true)
    public void testGetAllChatMessageBySenderAndRecipient_RecipientIdNull() {
        // Arrange
        getChatMessageDto.setRecipientId(null);

        // Act
        ResponseEntity<Object> response = apiAnyRoleRestController.getAllChatMessageBySenderAndRecipient(getChatMessageDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người nhận không tồn tại", response.getBody());
    }

    /**
     * TC_AARC_06: Test connecting to consultant
     *
     * Input: None (current user is authenticated)
     * Expected: Returns consultant user
     */
    @Test
    @DisplayName("TC_AARC_06: Test connecting to consultant")
    @Rollback(true)
    public void testConnectToConsultant() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(onlineUsers.findFirstROLE_TUVAN()).thenReturn(consultant);
        when(userService.findUserById(2)).thenReturn(consultant);
        when(chatRoomService.getChatRoomId(currentUser, consultant, true)).thenReturn(chatRoomId);

        // Act
        ResponseEntity<Object> response = apiAnyRoleRestController.connectToConsultant();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(consultant, response.getBody());
    }

    /**
     * TC_AARC_07: Test connecting to consultant when user is not logged in
     *
     * Input: None (no authenticated user)
     * Expected: Returns HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_AARC_07: Test connecting to consultant when user is not logged in")
    @Rollback(true)
    public void testConnectToConsultant_UserNotLoggedIn() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiAnyRoleRestController.connectToConsultant();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng không tồn tại", response.getBody());
    }

    /**
     * TC_AARC_08: Test connecting to consultant when no consultant is online
     *
     * Input: None (current user is authenticated)
     * Expected: Returns HTTP 204 No Content
     */
    @Test
    @DisplayName("TC_AARC_08: Test connecting to consultant when no consultant is online")
    @Rollback(true)
    public void testConnectToConsultant_NoConsultantOnline() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(onlineUsers.findFirstROLE_TUVAN()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiAnyRoleRestController.connectToConsultant();

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals("Hiện tại không có tư vấn viên nào đang hoạt động", response.getBody());
    }

    /**
     * TC_AARC_09: Test getting all recipients by sender
     *
     * Input: None (current user is authenticated)
     * Expected: Returns list of recipients with chat rooms
     */
    @Test
    @DisplayName("TC_AARC_09: Test getting all recipients by sender")
    @Rollback(true)
    public void testGetAllRecipientBySender() {
        // Arrange
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.add(chatMessage);

        ChatRoom chatRoom = new ChatRoom(chatRoomId, currentUser, consultant);
        chatRoom.setId(1);

        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(chatMessageService.findLatestMessagesBySenderAndSortChatRoomByLatestMessage(currentUser)).thenReturn(chatMessages);
        when(chatRoomService.findChatRoomByChatRoomIdAndSender(chatRoomId, currentUser)).thenReturn(chatRoom);

        // Act
        ResponseEntity<Object> response = apiAnyRoleRestController.getAllRecipientBySender();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    /**
     * TC_AARC_10: Test getting all recipients when user is not logged in
     *
     * Input: None (no authenticated user)
     * Expected: Returns HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_AARC_10: Test getting all recipients when user is not logged in")
    @Rollback(true)
    public void testGetAllRecipientBySender_UserNotLoggedIn() {
        // Arrange
        when(userService.getCurrentLoginUser()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiAnyRoleRestController.getAllRecipientBySender();

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng không tồn tại", response.getBody());
    }

    /**
     * TC_AARC_11: Test getting last chat message
     *
     * Input: RecipientDto with valid recipient ID
     * Expected: Returns the last chat message
     */
    @Test
    @DisplayName("TC_AARC_11: Test getting last chat message")
    @Rollback(true)
    public void testGetLastChatMessage() {
        // Arrange
        RecipientDto recipientDto = new RecipientDto();
        recipientDto.setRecipientId(2);

        when(userService.getCurrentLoginUser()).thenReturn(currentUser);
        when(userService.findUserById(2)).thenReturn(consultant);
        when(chatMessageService.findTopByOrderByCreatedDateDesc(currentUser, consultant)).thenReturn(chatMessages);

        // Act
        ResponseEntity<Object> response = apiAnyRoleRestController.getLastChatMessage(recipientDto);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(chatMessage, response.getBody());
    }

    /**
     * TC_AARC_12: Test getting last chat message when user is not logged in
     *
     * Input: RecipientDto with valid recipient ID, no authenticated user
     * Expected: Returns HTTP 404 Not Found
     */
    @Test
    @DisplayName("TC_AARC_12: Test getting last chat message when user is not logged in")
    @Rollback(true)
    public void testGetLastChatMessage_UserNotLoggedIn() {
        // Arrange
        RecipientDto recipientDto = new RecipientDto();
        recipientDto.setRecipientId(2);

        when(userService.getCurrentLoginUser()).thenReturn(null);

        // Act
        ResponseEntity<Object> response = apiAnyRoleRestController.getLastChatMessage(recipientDto);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Người dùng không tồn tại", response.getBody());
    }
}
