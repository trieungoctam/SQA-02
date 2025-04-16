package com.spring.privateClinicManage.service;

import java.util.List;

import com.spring.privateClinicManage.entity.ChatMessage;
import com.spring.privateClinicManage.entity.User;

public interface ChatMessageService {

	void save(ChatMessage chatMessage);

	ChatMessage saveChatMessage(ChatMessage chatMessage);

	List<ChatMessage> findBySenderAndRecipient(User sender, User recipient);

	List<ChatMessage> findTopByOrderByCreatedDateDesc(User sender, User recipient);

	List<ChatMessage> findLatestMessagesBySenderAndSortChatRoomByLatestMessage(User sender);

}
