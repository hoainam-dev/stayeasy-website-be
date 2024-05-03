package com.backend.stayEasy.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackTripDTO {
	private UUID feedbackId;
	private String content;
	private int rating;
	private LocalDateTime createAt;
	private UserDTO user;
	private PropertyDTO property;

}
