package com.backend.stayEasy.sevice;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.backend.stayEasy.convertor.FeedbackConverter;
import com.backend.stayEasy.dto.FeedbackDTO;
import com.backend.stayEasy.entity.Feedback;
import com.backend.stayEasy.repository.FeedbackRepository;

@Service
public class FeedbackService {
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private FeedbackRepository feedbackRepository;
	
	@Autowired
	private FeedbackConverter feedbackConverter;
	
	public Feedback addFeedback (Feedback feedback) {
		feedback.setCreateAt(LocalDateTime.now());
		Feedback savedFeedback = feedbackRepository.save(feedback);
		messagingTemplate.convertAndSend("/api/v1/stayeasy/topic/feedback", savedFeedback);
		return savedFeedback;
	}
	
}
