package com.spring.privateClinicManage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.spring.privateClinicManage.entity.ChatMessage;
import com.spring.privateClinicManage.entity.User;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer> {

	List<ChatMessage> findByChatRoomId(String chatId);

	@Query("SELECT c FROM ChatMessage c WHERE c.chatRoomId = :chatRoomId ORDER BY c.createdDate DESC")
	List<ChatMessage> findTopByOrderByCreatedDateDesc(@Param("chatRoomId") String chatRoomId);

	@Query("SELECT m FROM ChatMessage m " +
			"WHERE m.createdDate = (" +
			"    SELECT MAX(sub.createdDate) " +
			"    FROM ChatMessage sub " +
			"    WHERE sub.chatRoomId = m.chatRoomId" +
			") " + "AND (m.sender = :sender OR m.recipient = :sender) " +
			"ORDER BY m.createdDate DESC")
	List<ChatMessage> findLatestMessagesBySenderAndSortChatRoomByLatestMessage(User sender);
}
