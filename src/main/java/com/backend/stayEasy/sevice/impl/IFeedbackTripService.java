package com.backend.stayEasy.sevice.impl;

import java.util.List;
import java.util.UUID;

import com.backend.stayEasy.dto.FeedbackTripDTO;

public interface IFeedbackTripService {

	List<FeedbackTripDTO> getFeedback();

	FeedbackTripDTO add(FeedbackTripDTO feedbackDTO);

	FeedbackTripDTO update(UUID feedbackId, FeedbackTripDTO updaFeedbackDTO);

	void delete(UUID feedbackId);

	List<FeedbackTripDTO> getByPropertyId(UUID propertyId);

	FeedbackTripDTO getByUserIdAndPropertyId(UUID userId, UUID propertyId);

}
