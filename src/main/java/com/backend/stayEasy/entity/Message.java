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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="message")
public class Message {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="message_id")
	private UUID messageId;
	
	@Column(name="chat_room_id")
	private UUID chatRoomId;
	
	@Column(name="user_id")
	private UUID userId;
	@Column(name="content",columnDefinition = "NTEXT" )
	private String content;
	@Column(name = "createAt")
	private LocalDateTime createAt;
	
	@Column(name = "updateAt")
	private LocalDateTime updateAt;
	
}
