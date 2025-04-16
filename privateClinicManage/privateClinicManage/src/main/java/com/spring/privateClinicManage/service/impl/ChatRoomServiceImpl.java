package com.spring.privateClinicManage.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.privateClinicManage.entity.ChatRoom;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.repository.ChatRoomRepository;
import com.spring.privateClinicManage.service.ChatRoomService;

import jakarta.transaction.Transactional;

@Service
public class ChatRoomServiceImpl implements ChatRoomService {

	@Autowired
	private ChatRoomRepository chatRoomRepository;

	@Override
	@Transactional
	public void saveChatRoom(ChatRoom chatRoom) {
		chatRoomRepository.save(chatRoom);
	}

	@Override
	public ChatRoom findBySenderAndRecepient(User sender, User recepient) {
		return chatRoomRepository.findBySenderAndRecipient(sender, recepient);
	}

	@Override
	public String getChatRoomId(User sender, User recipient, Boolean createNewRoomIfNotExists) {
		ChatRoom chatRoom = chatRoomRepository.findBySenderAndRecipient(sender, recipient);
		if (chatRoom != null)
			return chatRoom.getChatRoomId();

		if (createNewRoomIfNotExists) {
			var chatId = createChatId(sender, recipient);
			return chatId;
		}

		return null;
	}

	@Override
	public String createChatId(User sender, User recipient) {

		var chatRoomId = String.format("%s_%s", sender.getId(), recipient.getId());

		ChatRoom senderRecipient = new ChatRoom(chatRoomId, sender, recipient);
		ChatRoom recipientSender = new ChatRoom(chatRoomId, recipient, sender);

		chatRoomRepository.save(senderRecipient);
		chatRoomRepository.save(recipientSender);

		return chatRoomId;
	}

	@Override
	public List<ChatRoom> findBySender(User sender) {
		return chatRoomRepository.findBySender(sender);
	}

	@Override
	public ChatRoom findChatRoomByChatRoomIdAndSenderOrRecipient(String chatRoomId, User sender,
			User recipient) {
		return chatRoomRepository.findByChatRoomIdAndSenderOrRecipient(chatRoomId, sender,
				recipient);
	}
	
	@Override
	public ChatRoom findChatRoomByChatRoomIdAndSender(String chatRoomId, User sender) {
		return chatRoomRepository.findByChatRoomIdAndSender(chatRoomId, sender);
	}

}
