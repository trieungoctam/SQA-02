package com.spring.privateClinicManage.service;

import java.util.List;

import com.spring.privateClinicManage.entity.ChatRoom;
import com.spring.privateClinicManage.entity.User;

public interface ChatRoomService {

	void saveChatRoom(ChatRoom chatRoom);

	ChatRoom findBySenderAndRecepient(User sender, User recepient);

	String getChatRoomId(User sender, User recipient, Boolean createNewRoomIfNotExists);

	List<ChatRoom> findBySender(User sender);

	String createChatId(User sender, User recipient);

	ChatRoom findChatRoomByChatRoomIdAndSenderOrRecipient(String chatRoomId, User sender,
			User recipient);

	ChatRoom findChatRoomByChatRoomIdAndSender(String chatRoomId, User sender);
}
