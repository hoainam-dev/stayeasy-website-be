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
@Table(name="notification")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="noti_id")
	private UUID notiId;
	
	@Column(name="sender_id")
	private UUID senderId;

	@Column(name="receiver_id")
	private UUID receiverId;

	@Column(name="content",columnDefinition = "nvarchar(255)" )
	private String content;

	@Column(name = "createAt")
	private LocalDateTime createAt;
	
	@Column(name = "updateAt")
	private LocalDateTime updateAt;
	
}
