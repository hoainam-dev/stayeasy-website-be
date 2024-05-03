package com.backend.stayEasy.sevice;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.stayEasy.convertor.FeedbackTripConverter;
import com.backend.stayEasy.dto.FeedbackTripDTO;
import com.backend.stayEasy.entity.FeedbackTrip;
import com.backend.stayEasy.repository.FeedbackTripRepository;
import com.backend.stayEasy.sevice.impl.IFeedbackTripService;

@Service
public class FeedbackTripService implements IFeedbackTripService{
	
	@Autowired
	private FeedbackTripRepository feedbackRepository;
	
	@Autowired
	private FeedbackTripConverter feedbackConverter;

	@Override
	public List<FeedbackTripDTO> getFeedback() {
		// TODO Auto-generated method stub
		List<FeedbackTripDTO> temp = new ArrayList<>();
		for (FeedbackTrip feedback : feedbackRepository.findAll()) {
			temp.add(feedbackConverter.toDTO(feedback));
		}
		return temp;
	}

	@Override
	public FeedbackTripDTO add(FeedbackTripDTO feedbackDTO) {
		return feedbackConverter.toDTO(feedbackRepository.save(feedbackConverter.toEntity(feedbackDTO)));
	}

	@Override
	public FeedbackTripDTO update(UUID feedbackId, FeedbackTripDTO updaFeedbackDTO) {
		Optional<FeedbackTrip> editFeedback = feedbackRepository.findById(feedbackId);
		if(!editFeedback.isEmpty()) {
			FeedbackTrip temp = editFeedback.get();
			temp.setContent(updaFeedbackDTO.getContent());
			temp.setRating(updaFeedbackDTO.getRating());
			return feedbackConverter.toDTO(feedbackRepository.save(temp));
		}else {
			return null;
		}
		
	}

	@Override
	public void delete(UUID feedbackId) {
		feedbackRepository.deleteById(feedbackId);
	}

	@Override
	public List<FeedbackTripDTO> getByPropertyId(UUID propertyId) {
		// TODO Auto-generated method stub
		List<FeedbackTripDTO> temp = new ArrayList<>();
		for (FeedbackTrip feedback : feedbackRepository.findByPropertyPropertyId(propertyId)) {
			temp.add(feedbackConverter.toDTO(feedback));
		}
		return temp;
	}

	@Override
	public FeedbackTripDTO getByUserIdAndPropertyId(UUID userId, UUID propertyId) {
	    // TODO: Implement your logic here
	    FeedbackTrip temp = feedbackRepository.findByPropertyPropertyIdAndUserId(propertyId, userId);
	    if (temp != null) {
	        return feedbackConverter.toDTO(temp);
	    } else {
	        return null;
	    }
	}

}
