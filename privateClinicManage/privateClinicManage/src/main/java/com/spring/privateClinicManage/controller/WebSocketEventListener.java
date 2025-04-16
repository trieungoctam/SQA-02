package com.spring.privateClinicManage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.spring.privateClinicManage.component.OnlinerUsers;
import com.spring.privateClinicManage.entity.User;
import com.spring.privateClinicManage.service.UserService;

@Controller
public class WebSocketEventListener {

	@Autowired
	private OnlinerUsers onlineUsers;
	@Autowired
	private UserService userService;


	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent sessionDisconnectEvent) {
		StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor
				.wrap(sessionDisconnectEvent.getMessage());
		String sessionId = stompHeaderAccessor.getSessionId();
		Integer userId = (Integer) stompHeaderAccessor.getSessionAttributes().get("userId");
		
		User currentUser = null;
		if (userId != null)
			currentUser = userService.findUserById(userId);
		if (currentUser != null)
			onlineUsers.findAndRemoveSessionIdByKey(currentUser.getRole().getName(), sessionId,
					currentUser.getId());
	}

	public boolean isUserActive(String userId) {
		return onlineUsers.getOnlineUsers().containsKey(userId);
	}

	



}
