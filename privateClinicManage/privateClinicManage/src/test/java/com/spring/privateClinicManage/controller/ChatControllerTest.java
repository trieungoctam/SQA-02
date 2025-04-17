package com.spring.privateClinicManage.controller;

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
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.spring.privateClinicManage.dto.ChatMessageDto;
import com.spring.privateClinicManage.dto.OnlineUserDto;
import com.spring.privateClinicManage.dto.OnlineUsersOutputDto;
import com.spring.privateClinicManage.entity.ChatMessage;
import com.spring.privateClinicManage.entity.Role;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.service.ChatMessageService;
import com.spring.privateClinicManage.service.UserService;
import com.spring.privateClinicManage.component.OnlinerUsers;
import com.spring.privateClinicManage.controller.WebSocketEventListener;

/**
 * Unit tests for ChatController
 *
 * This test class covers the main functionality of the ChatController,
 * which is used to handle WebSocket messages for the chat feature.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ChatControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ChatMessageService chatMessageService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Mock
    private OnlinerUsers onlineUsers;

    @InjectMocks
    private ChatController chatController;

    @InjectMocks
    private WebSocketEventListener webSocketEventListener;

    // Test data
    private User sender;
    private User recipient;
    private ChatMessage chatMessage;
    private ChatMessageDto chatMessageDto;
    private OnlineUserDto onlineUserDto;
    private String chatRoomId;
    private Date fixedDate;
    private Map<String, List<OnlineUsersOutputDto>> onlineUsersMap;

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
        sender = new User();
        sender.setId(1);
        sender.setEmail("patient@example.com");
        sender.setName("Patient User");
        sender.setRole(benhnhanRole);

        recipient = new User();
        recipient.setId(2);
        recipient.setEmail("consultant@example.com");
        recipient.setName("Consultant User");
        recipient.setRole(tuvanRole);

        // Create chat room ID
        chatRoomId = "1_2";

        // Mock ChatMessage
        chatMessage = new ChatMessage();
        chatMessage.setId(1);
        chatMessage.setSender(sender);
        chatMessage.setRecipient(recipient);
        chatMessage.setChatRoomId(chatRoomId);
        chatMessage.setContent("Hello, this is a test message");
        chatMessage.setCreatedDate(fixedDate);

        // Mock ChatMessageDto
        chatMessageDto = new ChatMessageDto();
        chatMessageDto.setSenderId(1);
        chatMessageDto.setRecipientId(2);
        chatMessageDto.setContent("Hello, this is a test message");
        chatMessageDto.setCreatedDate(fixedDate);

        // Mock OnlineUserDto
        onlineUserDto = new OnlineUserDto();
        onlineUserDto.setUserId(1);

        // Mock online users map
        onlineUsersMap = new HashMap<>();
        List<OnlineUsersOutputDto> benhnhanList = new ArrayList<>();
        benhnhanList.add(new OnlineUsersOutputDto(sender, "session1"));
        onlineUsersMap.put("ROLE_BENHNHAN", benhnhanList);

        List<OnlineUsersOutputDto> tuvanList = new ArrayList<>();
        tuvanList.add(new OnlineUsersOutputDto(recipient, "session2"));
        onlineUsersMap.put("ROLE_TUVAN", tuvanList);
    }

    /**
     * TC_CC_01: Test adding an online user
     *
     * Input: Valid OnlineUserDto and SimpMessageHeaderAccessor
     * Expected: User is added to online users list
     */
    @Test
    @DisplayName("TC_CC_01: Test adding an online user")
    @Rollback(true)
    public void testAddUser() {
        // Arrange
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId("session1");
        Map<String, Object> sessionAttributes = new HashMap<>();
        headerAccessor.setSessionAttributes(sessionAttributes);

        when(userService.findUserById(1)).thenReturn(sender);
        when(onlineUsers.getOnlineUsers()).thenReturn(onlineUsersMap);

        // Act
        chatController.addUser(onlineUserDto, headerAccessor);

        // Assert
        assertEquals(1, sessionAttributes.get("userId"));
        verify(messagingTemplate).convertAndSend("/online-users", sender);
    }

    /**
     * TC_CC_02: Test adding an online user when user doesn't exist
     *
     * Input: OnlineUserDto with invalid user ID and SimpMessageHeaderAccessor
     * Expected: No user is added to online users list
     */
    @Test
    @DisplayName("TC_CC_02: Test adding an online user when user doesn't exist")
    @Rollback(true)
    public void testAddUser_UserNotFound() {
        // Arrange
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId("session1");
        Map<String, Object> sessionAttributes = new HashMap<>();
        headerAccessor.setSessionAttributes(sessionAttributes);

        when(userService.findUserById(1)).thenReturn(null);

        // Act
        chatController.addUser(onlineUserDto, headerAccessor);

        // Assert
        assertEquals(1, sessionAttributes.get("userId"));
        verify(messagingTemplate, never()).convertAndSend("/online-users", sender);
    }

    /**
     * TC_CC_03: Test processing a chat message
     *
     * Input: Valid ChatMessageDto
     * Expected: ChatMessage is saved and sent to recipient
     */
    @Test
    @DisplayName("TC_CC_03: Test processing a chat message")
    @Rollback(true)
    public void testProcessMessage() {
        // Arrange
        when(userService.findUserById(1)).thenReturn(sender);
        when(userService.findUserById(2)).thenReturn(recipient);
        when(chatMessageService.saveChatMessage(any(ChatMessage.class))).thenReturn(chatMessage);

        // Act
        chatController.processMessage(chatMessageDto);

        // Assert
        verify(chatMessageService).saveChatMessage(any(ChatMessage.class));
        verify(messagingTemplate).convertAndSendToUser(
                eq("2"),
                eq("/queue/messages"),
                any(ChatMessage.class));
    }

    /**
     * TC_CC_04: Test handling WebSocket disconnect event
     *
     * Input: Valid SessionDisconnectEvent
     * Expected: User is removed from online users list
     */
    @Test
    @DisplayName("TC_CC_04: Test handling WebSocket disconnect event")
    @Rollback(true)
    public void testHandleWebSocketDisconnectListener() {
        // Arrange
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("userId", 1);

        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId("session1");
        headerAccessor.setSessionAttributes(sessionAttributes);

        Message<byte[]> message = new GenericMessage<>(new byte[0], headerAccessor.getMessageHeaders());
        CloseStatus closeStatus = CloseStatus.NORMAL;
        SessionDisconnectEvent event = new SessionDisconnectEvent(this, message, "session1", closeStatus);

        when(userService.findUserById(1)).thenReturn(sender);

        // Act
        webSocketEventListener.handleWebSocketDisconnectListener(event);

        // Assert
        verify(onlineUsers).findAndRemoveSessionIdByKey("ROLE_BENHNHAN", "session1", 1);
    }

    /**
     * TC_CC_05: Test handling WebSocket disconnect event when user doesn't exist
     *
     * Input: SessionDisconnectEvent with invalid user ID
     * Expected: No user is removed from online users list
     */
    @Test
    @DisplayName("TC_CC_05: Test handling WebSocket disconnect event when user doesn't exist")
    @Rollback(true)
    public void testHandleWebSocketDisconnectListener_UserNotFound() {
        // Arrange
        Map<String, Object> sessionAttributes = new HashMap<>();
        sessionAttributes.put("userId", 999);

        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId("session1");
        headerAccessor.setSessionAttributes(sessionAttributes);

        Message<byte[]> message = new GenericMessage<>(new byte[0], headerAccessor.getMessageHeaders());
        CloseStatus closeStatus = CloseStatus.NORMAL;
        SessionDisconnectEvent event = new SessionDisconnectEvent(this, message, "session1", closeStatus);

        when(userService.findUserById(999)).thenReturn(null);

        // Act
        webSocketEventListener.handleWebSocketDisconnectListener(event);

        // Assert
        verify(onlineUsers, never()).findAndRemoveSessionIdByKey(anyString(), anyString(), anyInt());
    }
}
