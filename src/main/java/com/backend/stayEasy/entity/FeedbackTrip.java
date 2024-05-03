package com.backend.stayEasy.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="feedback-trip")
public class FeedbackTrip {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="feedback_id")
	private UUID feedbackId;
	
	@Column(name="content",columnDefinition = "NTEXT" )
	private String content;
	
	@Column(name = "createAt")
	private LocalDateTime createAt;
	
	@Column(name="rating")
	private int rating;
	
	@OneToOne()
	private Property property;
	
	@OneToOne()
	private User user;
}
