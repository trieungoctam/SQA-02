package com.spring.privateClinicManage.dto;

import com.spring.privateClinicManage.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUsersOutputDto {
	private User user;
	private String sessionId;
}
