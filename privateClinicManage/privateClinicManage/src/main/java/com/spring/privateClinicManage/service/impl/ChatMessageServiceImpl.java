package com.spring.privateClinicManage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.ChatMessage;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.ChatMessageRepository;
import com.spring.privateClinicManage.service.ChatMessageService;
import com.spring.privateClinicManage.service.ChatRoomService;

import jakarta.transaction.Transactional;

@Service
@org.springframework.transaction.annotation.Transactional
public class ChatMessageServiceImpl implements ChatMessageService {

	@Autowired
	private ChatRoomService chatRoomService;
	@Autowired
	private ChatMessageRepository chatMessageRepository;

	@Override
	@Transactional
	public ChatMessage saveChatMessage(ChatMessage chatMessage) {
		String chatRoomId = chatRoomService.getChatRoomId(chatMessage.getSender(),
				chatMessage.getRecipient(), true);

		chatMessage.setChatRoomId(chatRoomId);
		chatMessageRepository.save(chatMessage);

		return chatMessage;
	}

	@Override
	public List<ChatMessage> findBySenderAndRecipient(User sender, User recipient) {

		String chatRoomId = chatRoomService.getChatRoomId(sender, recipient, false);

		return chatMessageRepository.findByChatRoomId(chatRoomId);
	}

	@Override
	@Transactional
	public void save(ChatMessage chatMessage) {
		chatMessageRepository.save(chatMessage);
	}

	@Override
	public List<ChatMessage> findTopByOrderByCreatedDateDesc(User sender, User recipient) {
		String chatRoomId = chatRoomService.getChatRoomId(sender, recipient, false);
		return chatMessageRepository.findTopByOrderByCreatedDateDesc(chatRoomId);
	}

	@Override
	public List<ChatMessage> findLatestMessagesBySenderAndSortChatRoomByLatestMessage(User sender) {
		return chatMessageRepository
				.findLatestMessagesBySenderAndSortChatRoomByLatestMessage(sender);
	}

}
