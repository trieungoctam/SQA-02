package com.spring.privateClinicManage.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.spring.privateClinicManage.entity.ChatRoom;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.ChatRoomRepository;
import com.spring.privateClinicManage.service.impl.ChatRoomServiceImpl;

/**
 * Unit tests for ChatRoomServiceImpl
 * This class tests the chat room service implementation for the consultation functionality
 */
@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

    // Test data
    private ChatRoom testChatRoom;
    private User sender;
    private User recipient;
    private String chatRoomId;
    private List<ChatRoom> chatRoomList;

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

        // Create test chat room
        testChatRoom = new ChatRoom(chatRoomId, sender, recipient);
        testChatRoom.setId(1);

        // Create chat room list
        chatRoomList = new ArrayList<>();
        chatRoomList.add(testChatRoom);
    }

    /**
     * Test case: TC_CHAT_ROOM_01
     * Test saving a valid ChatRoom object
     * Input: Valid ChatRoom object
     * Expected output: Repository save method is called with the correct object
     */
    @Test
    @DisplayName("TC_CHAT_ROOM_01: Save valid ChatRoom")
    void testSaveChatRoom_ValidChatRoom() {
        // Arrange
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(testChatRoom);

        // Act
        chatRoomService.saveChatRoom(testChatRoom);

        // Assert
        verify(chatRoomRepository, times(1)).save(testChatRoom);
    }

    /**
     * Test case: TC_CHAT_ROOM_02
     * Test finding a chat room by sender and recipient when it exists
     * Input: Valid sender and recipient User objects
     * Expected output: The corresponding ChatRoom object
     */
    @Test
    @DisplayName("TC_CHAT_ROOM_02: Find chat room by sender and recipient when it exists")
    void testFindBySenderAndRecepient_ExistingChatRoom() {
        // Arrange
        when(chatRoomRepository.findBySenderAndRecipient(sender, recipient)).thenReturn(testChatRoom);

        // Act
        ChatRoom result = chatRoomService.findBySenderAndRecepient(sender, recipient);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(chatRoomId, result.getChatRoomId());
        assertEquals(sender, result.getSender());
        assertEquals(recipient, result.getRecipient());
        verify(chatRoomRepository, times(1)).findBySenderAndRecipient(sender, recipient);
    }

    /**
     * Test case: TC_CHAT_ROOM_03
     * Test getting a chat room ID when the chat room exists
     * Input: Valid sender and recipient User objects with existing chat room
     * Expected output: The existing chat room ID
     */
    @Test
    @DisplayName("TC_CHAT_ROOM_03: Get chat room ID when chat room exists")
    void testGetChatRoomId_ExistingChatRoom() {
        // Arrange
        when(chatRoomRepository.findBySenderAndRecipient(sender, recipient)).thenReturn(testChatRoom);

        // Act
        String result = chatRoomService.getChatRoomId(sender, recipient, false);

        // Assert
        assertEquals(chatRoomId, result);
        verify(chatRoomRepository, times(1)).findBySenderAndRecipient(sender, recipient);
        // Verify that save is not called since we're not creating a new room
        verify(chatRoomRepository, never()).save(any(ChatRoom.class));
    }

    /**
     * Test case: TC_CHAT_ROOM_04
     * Test getting a chat room ID when the chat room doesn't exist and createNewRoomIfNotExists is true
     * Input: Valid sender and recipient User objects with no existing chat room
     * Expected output: A new chat room ID
     */
    @Test
    @DisplayName("TC_CHAT_ROOM_04: Get chat room ID when chat room doesn't exist and createNewRoomIfNotExists is true")
    void testGetChatRoomId_NonExistingChatRoom_CreateNew() {
        // Arrange
        when(chatRoomRepository.findBySenderAndRecipient(sender, recipient)).thenReturn(null);
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(new ChatRoom());

        // Act
        String result = chatRoomService.getChatRoomId(sender, recipient, true);

        // Assert
        assertNotNull(result);
        assertEquals("1_2", result);
        verify(chatRoomRepository, times(1)).findBySenderAndRecipient(sender, recipient);
        // Verify that save is called twice (once for sender->recipient, once for recipient->sender)
        verify(chatRoomRepository, times(2)).save(any(ChatRoom.class));
    }

    /**
     * Test case: TC_CHAT_ROOM_05
     * Test getting a chat room ID when the chat room doesn't exist and createNewRoomIfNotExists is false
     * Input: Valid sender and recipient User objects with no existing chat room
     * Expected output: null
     */
    @Test
    @DisplayName("TC_CHAT_ROOM_05: Get chat room ID when chat room doesn't exist and createNewRoomIfNotExists is false")
    void testGetChatRoomId_NonExistingChatRoom_DontCreateNew() {
        // Arrange
        when(chatRoomRepository.findBySenderAndRecipient(sender, recipient)).thenReturn(null);

        // Act
        String result = chatRoomService.getChatRoomId(sender, recipient, false);

        // Assert
        assertNull(result);
        verify(chatRoomRepository, times(1)).findBySenderAndRecipient(sender, recipient);
        // Verify that save is not called since we're not creating a new room
        verify(chatRoomRepository, never()).save(any(ChatRoom.class));
    }

    /**
     * Test case: TC_CHAT_ROOM_06
     * Test creating a chat ID
     * Input: Valid sender and recipient User objects
     * Expected output: A new chat room ID
     */
    @Test
    @DisplayName("TC_CHAT_ROOM_06: Create chat ID")
    void testCreateChatId() {
        // Arrange
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(new ChatRoom());

        // Act
        String result = chatRoomService.createChatId(sender, recipient);

        // Assert
        assertNotNull(result);
        assertEquals("1_2", result);
        // Verify that save is called twice (once for sender->recipient, once for recipient->sender)
        verify(chatRoomRepository, times(2)).save(any(ChatRoom.class));
    }

    /**
     * Test case: TC_CHAT_ROOM_07
     * Test finding chat rooms by sender
     * Input: Valid sender User object
     * Expected output: List of chat rooms for the sender
     */
    @Test
    @DisplayName("TC_CHAT_ROOM_07: Find chat rooms by sender")
    void testFindBySender() {
        // Arrange
        when(chatRoomRepository.findBySender(sender)).thenReturn(chatRoomList);

        // Act
        List<ChatRoom> result = chatRoomService.findBySender(sender);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(chatRoomId, result.get(0).getChatRoomId());
        verify(chatRoomRepository, times(1)).findBySender(sender);
    }

    /**
     * Test case: TC_CHAT_ROOM_08
     * Test finding a chat room by chat room ID, sender, and recipient
     * Input: Valid chat room ID, sender, and recipient User objects
     * Expected output: The corresponding ChatRoom object
     */
    @Test
    @DisplayName("TC_CHAT_ROOM_08: Find chat room by chat room ID, sender, and recipient")
    void testFindChatRoomByChatRoomIdAndSenderOrRecipient() {
        // Arrange
        when(chatRoomRepository.findByChatRoomIdAndSenderOrRecipient(chatRoomId, sender, recipient)).thenReturn(testChatRoom);

        // Act
        ChatRoom result = chatRoomService.findChatRoomByChatRoomIdAndSenderOrRecipient(chatRoomId, sender, recipient);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(chatRoomId, result.getChatRoomId());
        verify(chatRoomRepository, times(1)).findByChatRoomIdAndSenderOrRecipient(chatRoomId, sender, recipient);
    }

    /**
     * Test case: TC_CHAT_ROOM_09
     * Test finding a chat room by chat room ID and sender
     * Input: Valid chat room ID and sender User object
     * Expected output: The corresponding ChatRoom object
     */
    @Test
    @DisplayName("TC_CHAT_ROOM_09: Find chat room by chat room ID and sender")
    void testFindChatRoomByChatRoomIdAndSender() {
        // Arrange
        when(chatRoomRepository.findByChatRoomIdAndSender(chatRoomId, sender)).thenReturn(testChatRoom);

        // Act
        ChatRoom result = chatRoomService.findChatRoomByChatRoomIdAndSender(chatRoomId, sender);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(chatRoomId, result.getChatRoomId());
        verify(chatRoomRepository, times(1)).findByChatRoomIdAndSender(chatRoomId, sender);
    }
}
