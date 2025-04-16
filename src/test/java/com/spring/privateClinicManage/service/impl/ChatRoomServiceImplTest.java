package com.spring.privateClinicManage.service.impl;

import com.spring.privateClinicManage.entity.ChatRoom;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ChatRoomServiceImpl class
 * Tests the functionality related to chat rooms for consultations
 */
@ExtendWith(MockitoExtension.class)
public class ChatRoomServiceImplTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;

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
     * Test case ID: CHAT-ROOM-SERVICE-01
     * Test objective: Verify that a chat room can be saved successfully
     * Input: A valid ChatRoom object
     * Expected output: ChatRoom is saved in the repository
     */
    @Test
    @DisplayName("Test saveChatRoom method")
    void testSaveChatRoom() {
        // Arrange
        doNothing().when(chatRoomRepository).save(any(ChatRoom.class));

        // Act
        chatRoomService.saveChatRoom(testChatRoom);

        // Assert
        verify(chatRoomRepository, times(1)).save(testChatRoom);
    }

    /**
     * Test case ID: CHAT-ROOM-SERVICE-02
     * Test objective: Verify that findBySenderAndRecepient returns a chat room when it exists
     * Input: Valid sender and recipient User objects
     * Expected output: The corresponding ChatRoom object
     */
    @Test
    @DisplayName("Test findBySenderAndRecepient method with existing chat room")
    void testFindBySenderAndRecepientWithExistingChatRoom() {
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
     * Test case ID: CHAT-ROOM-SERVICE-03
     * Test objective: Verify that getChatRoomId returns existing chat room ID
     * Input: Valid sender and recipient User objects with existing chat room
     * Expected output: The existing chat room ID
     */
    @Test
    @DisplayName("Test getChatRoomId method with existing chat room")
    void testGetChatRoomIdWithExistingChatRoom() {
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
     * Test case ID: CHAT-ROOM-SERVICE-04
     * Test objective: Verify that getChatRoomId creates new chat room when it doesn't exist
     * Input: Valid sender and recipient User objects with no existing chat room
     * Expected output: A new chat room ID
     */
    @Test
    @DisplayName("Test getChatRoomId method with no existing chat room")
    void testGetChatRoomIdWithNoExistingChatRoom() {
        // Arrange
        when(chatRoomRepository.findBySenderAndRecipient(sender, recipient)).thenReturn(null);
        doNothing().when(chatRoomRepository).save(any(ChatRoom.class));

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
     * Test case ID: CHAT-ROOM-SERVICE-05
     * Test objective: Verify that getChatRoomId returns null when chat room doesn't exist and createNewRoomIfNotExists is false
     * Input: Valid sender and recipient User objects with no existing chat room
     * Expected output: null
     */
    @Test
    @DisplayName("Test getChatRoomId method with no existing chat room and createNewRoomIfNotExists=false")
    void testGetChatRoomIdWithNoExistingChatRoomAndNoCreate() {
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
     * Test case ID: CHAT-ROOM-SERVICE-06
     * Test objective: Verify that createChatId creates a new chat room
     * Input: Valid sender and recipient User objects
     * Expected output: A new chat room ID
     */
    @Test
    @DisplayName("Test createChatId method")
    void testCreateChatId() {
        // Arrange
        doNothing().when(chatRoomRepository).save(any(ChatRoom.class));

        // Act
        String result = chatRoomService.createChatId(sender, recipient);

        // Assert
        assertNotNull(result);
        assertEquals("1_2", result);
        // Verify that save is called twice (once for sender->recipient, once for recipient->sender)
        verify(chatRoomRepository, times(2)).save(any(ChatRoom.class));
    }

    /**
     * Test case ID: CHAT-ROOM-SERVICE-07
     * Test objective: Verify that findBySender returns chat rooms for a specific sender
     * Input: Valid sender User object
     * Expected output: List of chat rooms for the sender
     */
    @Test
    @DisplayName("Test findBySender method")
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
     * Test case ID: CHAT-ROOM-SERVICE-08
     * Test objective: Verify that findChatRoomByChatRoomIdAndSenderOrRecipient returns a chat room
     * Input: Valid chat room ID, sender, and recipient User objects
     * Expected output: The corresponding ChatRoom object
     */
    @Test
    @DisplayName("Test findChatRoomByChatRoomIdAndSenderOrRecipient method")
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
     * Test case ID: CHAT-ROOM-SERVICE-09
     * Test objective: Verify that findChatRoomByChatRoomIdAndSender returns a chat room
     * Input: Valid chat room ID and sender User object
     * Expected output: The corresponding ChatRoom object
     */
    @Test
    @DisplayName("Test findChatRoomByChatRoomIdAndSender method")
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
