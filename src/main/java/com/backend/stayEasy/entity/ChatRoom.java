package com.backend.stayEasy.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="chatroom")
public class ChatRoom {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="chat_room_id")
	private UUID roomChatId;

	@Column(name="user_id")
	private UUID userId;
	
	@Column(name="host_id")
	private UUID hostId;
	
	@Column(name = "createAt")
	private LocalDateTime createAt;
	
	@Column(name = "updateAt")
	private LocalDateTime updateAt;
}
