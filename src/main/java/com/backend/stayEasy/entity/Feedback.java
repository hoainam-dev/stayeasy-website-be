package com.backend.stayEasy.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="feedback")
public class Feedback {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="feedback_id")
	private UUID feedbackId;

	
	@Column(name="content",columnDefinition = "NTEXT" )
	private String content;
	
	@Column(name = "createAt")
	private LocalDateTime createAt;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "avatar")
	private String avatar;
		
	@Column(name = "userId")
	private UUID userId;
	
	@Column(name = "property_id")
	private UUID propertyId;
	

}