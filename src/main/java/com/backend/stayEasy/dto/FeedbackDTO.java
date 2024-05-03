package com.backend.stayEasy.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackDTO {
	private UUID feedbackId;
	private String content;
	private String username;
	private String avatar;
	private UUID userId;
	private UUID propertyId;
}
