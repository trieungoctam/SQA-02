package com.spring.privateClinicManage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.ChatRoom;
import com.spring.privateClinicManage.entity.User;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer> {

	ChatRoom findBySenderAndRecipient(User sender, User recipient);

	List<ChatRoom> findBySender(User sender);

	ChatRoom findByChatRoomIdAndSenderOrRecipient(String chatRoomId, User sender, User recipient);

	ChatRoom findByChatRoomIdAndSender(String chatRoomId, User sender);

}
