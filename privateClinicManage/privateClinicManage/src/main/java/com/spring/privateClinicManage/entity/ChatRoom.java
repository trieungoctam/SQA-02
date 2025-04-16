package com.spring.privateClinicManage.entity;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chatroom")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "chat_room_id")
	private String chatRoomId;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH
	}) // không thê để persist
	@JoinColumn(name = "sender_id", referencedColumnName = "id")
	private User sender;

	@ManyToOne(fetch = FetchType.EAGER, cascade = {
			CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH
	}) // không thê để persist
	@JoinColumn(name = "recipient_id", referencedColumnName = "id")
	private User recipient;

	public ChatRoom(String chatId, User sender, User recipient) {
		this.chatRoomId = chatId;
		this.sender = sender;
		this.recipient = recipient;
	}

}
