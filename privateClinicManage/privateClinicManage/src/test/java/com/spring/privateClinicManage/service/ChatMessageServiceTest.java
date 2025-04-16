package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spring.privateClinicManage.entity.ChatMessage;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.ChatMessageRepository;
import com.spring.privateClinicManage.service.impl.ChatMessageServiceImpl;

/**
 * Unit tests for ChatMessageServiceImpl
 * This class tests the chat message service implementation for the consultation functionality
 */
@ExtendWith(MockitoExtension.class)
public class ChatMessageServiceTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRoomService chatRoomService;

    @InjectMocks
    private ChatMessageServiceImpl chatMessageService;

    // Test data
    private ChatMessage testChatMessage;
    private User sender;
    private User recipient;
    private String chatRoomId;
    private List<ChatMessage> chatMessageList;

    /**
     * Setup test data before each test
     */
    @BeforeEach
    void setUp() {
        // Create test users
        sender = new User();
        sender.setId(1);
        sender.setName("Sender User");
        sender.setEmail("sender@example.com");
        sender.setAvatar("sender-avatar.jpg");

        recipient = new User();
        recipient.setId(2);
        recipient.setName("Recipient User");
        recipient.setEmail("recipient@example.com");
        recipient.setAvatar("recipient-avatar.jpg");

        // Create chat room ID
        chatRoomId = "1_2";

        // Create test chat message
        testChatMessage = new ChatMessage();
        testChatMessage.setId(1);
        testChatMessage.setSender(sender);
        testChatMessage.setRecipient(recipient);
        testChatMessage.setContent("Hello, this is a test message");
        testChatMessage.setCreatedDate(new Date());
        testChatMessage.setChatRoomId(chatRoomId);

        // Create chat message list
        chatMessageList = new ArrayList<>();
        chatMessageList.add(testChatMessage);
    }

    /**
     * Test case: TC_CHAT_MESSAGE_01
     * Test saving a valid ChatMessage object
     * Input: Valid ChatMessage object
     * Expected output: Repository save method is called with the correct object
     */
    @Test
    @DisplayName("TC_CHAT_MESSAGE_01: Save valid ChatMessage")
    void testSave_ValidChatMessage() {
        // Arrange
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(testChatMessage);

        // Act
        chatMessageService.save(testChatMessage);

        // Assert
        verify(chatMessageRepository, times(1)).save(testChatMessage);
    }

    /**
     * Test case: TC_CHAT_MESSAGE_02
     * Test saving a chat message with the correct chat room ID
     * Input: Valid ChatMessage object without chat room ID
     * Expected output: ChatMessage is saved with the correct chat room ID
     */
    @Test
    @DisplayName("TC_CHAT_MESSAGE_02: Save chat message with correct chat room ID")
    void testSaveChatMessage_WithChatRoomId() {
        // Arrange
        ChatMessage messageWithoutChatRoomId = new ChatMessage();
        messageWithoutChatRoomId.setId(2);
        messageWithoutChatRoomId.setSender(sender);
        messageWithoutChatRoomId.setRecipient(recipient);
        messageWithoutChatRoomId.setContent("Another test message");
        messageWithoutChatRoomId.setCreatedDate(new Date());
        
        when(chatRoomService.getChatRoomId(sender, recipient, true)).thenReturn(chatRoomId);
        when(chatMessageRepository.save(any(ChatMessage.class))).thenReturn(messageWithoutChatRoomId);

        // Act
        ChatMessage result = chatMessageService.saveChatMessage(messageWithoutChatRoomId);

        // Assert
        assertNotNull(result);
        assertEquals(chatRoomId, result.getChatRoomId());
        verify(chatRoomService, times(1)).getChatRoomId(sender, recipient, true);
        verify(chatMessageRepository, times(1)).save(messageWithoutChatRoomId);
    }

    /**
     * Test case: TC_CHAT_MESSAGE_03
     * Test finding messages between sender and recipient
     * Input: Valid sender and recipient User objects
     * Expected output: List of messages between the sender and recipient
     */
    @Test
    @DisplayName("TC_CHAT_MESSAGE_03: Find messages between sender and recipient")
    void testFindBySenderAndRecipient() {
        // Arrange
        when(chatRoomService.getChatRoomId(sender, recipient, false)).thenReturn(chatRoomId);
        when(chatMessageRepository.findByChatRoomId(chatRoomId)).thenReturn(chatMessageList);

        // Act
        List<ChatMessage> result = chatMessageService.findBySenderAndRecipient(sender, recipient);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hello, this is a test message", result.get(0).getContent());
        verify(chatRoomService, times(1)).getChatRoomId(sender, recipient, false);
        verify(chatMessageRepository, times(1)).findByChatRoomId(chatRoomId);
    }

    /**
     * Test case: TC_CHAT_MESSAGE_04
     * Test finding latest messages between sender and recipient
     * Input: Valid sender and recipient User objects
     * Expected output: List of latest messages
     */
    @Test
    @DisplayName("TC_CHAT_MESSAGE_04: Find latest messages between sender and recipient")
    void testFindTopByOrderByCreatedDateDesc() {
        // Arrange
        when(chatRoomService.getChatRoomId(sender, recipient, false)).thenReturn(chatRoomId);
        when(chatMessageRepository.findTopByOrderByCreatedDateDesc(chatRoomId)).thenReturn(chatMessageList);

        // Act
        List<ChatMessage> result = chatMessageService.findTopByOrderByCreatedDateDesc(sender, recipient);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hello, this is a test message", result.get(0).getContent());
        verify(chatRoomService, times(1)).getChatRoomId(sender, recipient, false);
        verify(chatMessageRepository, times(1)).findTopByOrderByCreatedDateDesc(chatRoomId);
    }

    /**
     * Test case: TC_CHAT_MESSAGE_05
     * Test finding latest messages by sender and sorting by chat room
     * Input: Valid sender User object
     * Expected output: List of latest messages sorted by chat room
     */
    @Test
    @DisplayName("TC_CHAT_MESSAGE_05: Find latest messages by sender and sort by chat room")
    void testFindLatestMessagesBySenderAndSortChatRoomByLatestMessage() {
        // Arrange
        when(chatMessageRepository.findLatestMessagesBySenderAndSortChatRoomByLatestMessage(sender)).thenReturn(chatMessageList);

        // Act
        List<ChatMessage> result = chatMessageService.findLatestMessagesBySenderAndSortChatRoomByLatestMessage(sender);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hello, this is a test message", result.get(0).getContent());
        verify(chatMessageRepository, times(1)).findLatestMessagesBySenderAndSortChatRoomByLatestMessage(sender);
    }
}
