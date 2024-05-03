package com.backend.stayEasy.convertor;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.backend.stayEasy.dto.FeedbackTripDTO;
import com.backend.stayEasy.entity.FeedbackTrip;
import com.backend.stayEasy.entity.Property;
import com.backend.stayEasy.entity.User;
import com.backend.stayEasy.repository.IPropertyRepository;
import com.backend.stayEasy.repository.UserRepository;

@Component
public class FeedbackTripConverter {
	
	@Autowired
	private PropertyConverter propertyConverter;
	
	@Autowired
	private UserConverter userConverter;
	
	@Autowired 
	private IPropertyRepository propertyRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	public FeedbackTripDTO toDTO(FeedbackTrip feedback) {
		FeedbackTripDTO feedbackDTO = new FeedbackTripDTO();
		feedbackDTO.setContent(feedback.getContent());
		feedbackDTO.setFeedbackId(feedback.getFeedbackId());
		feedbackDTO.setRating(feedback.getRating());
		feedbackDTO.setCreateAt(feedback.getCreateAt());
		feedbackDTO.setProperty(propertyConverter.toDTO(feedback.getProperty()));
		feedbackDTO.setUser(userConverter.toDTO(feedback.getUser()));
		return feedbackDTO;
	}

	public FeedbackTrip toEntity(FeedbackTripDTO feedbackDTO) {
		FeedbackTrip feedback = new FeedbackTrip();
		if(feedbackDTO.getFeedbackId()!=null) {
			feedback.setFeedbackId(feedbackDTO.getFeedbackId());
		}
		if(feedbackDTO.getCreateAt()!=null) {
			feedback.setCreateAt(feedbackDTO.getCreateAt());
		}else {
			feedback.setCreateAt(LocalDateTime.now());
		}
		feedback.setContent(feedbackDTO.getContent());
		feedback.setRating(feedbackDTO.getRating());
		
		Optional<Property> tempProperty = propertyRepository.findById(feedbackDTO.getProperty().getPropertyId());
		Optional<User> tempUser = userRepository.findById(feedbackDTO.getUser().getId());
		if(!tempProperty.isEmpty())
			feedback.setProperty(tempProperty.get());
		;
		if(!tempUser.isEmpty()) {
			feedback.setUser(tempUser.get());
		}
		return feedback;
	}
}
