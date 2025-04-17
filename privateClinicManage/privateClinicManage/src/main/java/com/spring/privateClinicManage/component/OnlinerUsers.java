package com.spring.privateClinicManage.component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.spring.privateClinicManage.dto.OnlineUsersOutputDto;
import com.spring.privateClinicManage.entity.User;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
public class OnlinerUsers {
	private Map<String, List<OnlineUsersOutputDto>> onlineUsers = new HashMap<>();

	public OnlinerUsers() {
		this.onlineUsers = new HashMap<>();
	}

	public void findAndRemoveSessionIdByKey(String key, String sessionId, Integer userId) {
		List<OnlineUsersOutputDto> usersList = onlineUsers.get(key);
		if (usersList != null && usersList.size() > 0) {
			for (int i = 0; i < usersList.size(); i++) {
				OnlineUsersOutputDto userOutput = usersList.get(i);
				if (userOutput.getSessionId().equals(sessionId)
						|| userOutput.getUser().getId().equals(userId)) {
						usersList.remove(i);
					this.getOnlineUsers().put(key, usersList);
				}
			}
		}
	}

	public User findFirstROLE_TUVAN() {
		List<OnlineUsersOutputDto> usersList = onlineUsers.get("ROLE_TUVAN");
		if (usersList == null || usersList.size() < 1)
			return null;

		return usersList.get(0).getUser();
	}

	public Boolean isUserOnline(User user) {

		List<OnlineUsersOutputDto> usersList = onlineUsers.get(user.getRole().getName());

		if (usersList != null && usersList.size() > 0) {
			for (int i = 0; i < usersList.size(); i++) {
				OnlineUsersOutputDto userOutput = usersList.get(i);
				if (userOutput.getUser().getId().equals(user.getId()))
					return true;
			}
		}
		return false;
	}

}
