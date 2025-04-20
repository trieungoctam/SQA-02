package com.spring.privateClinicManage.dto;

import com.spring.privateClinicManage.entity.ChatMessage;
import com.spring.privateClinicManage.entity.ChatRoom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipientChatRoomDto {
	private ChatRoom chatRoom;
	private ChatMessage chatMessage;
}
