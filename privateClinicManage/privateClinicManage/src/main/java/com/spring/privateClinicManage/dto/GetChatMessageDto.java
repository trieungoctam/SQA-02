package com.spring.privateClinicManage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetChatMessageDto {
	private Integer senderId;
	private Integer recipientId;
}
