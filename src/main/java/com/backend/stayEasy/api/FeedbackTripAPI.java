package com.backend.stayEasy.api;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stayEasy.dto.FeedbackTripDTO;
import com.backend.stayEasy.sevice.impl.IFeedbackTripService;



@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping(value = "/api/v1/stayeasy/trip/feedback", produces = "application/json")
public class FeedbackTripAPI {
	
	@Autowired
	private IFeedbackTripService feedbackService;
	
	@GetMapping("")
	public List<FeedbackTripDTO> getFeedback(){
		return feedbackService.getFeedback();
	}
	
	@GetMapping("/{id}")
	public List<FeedbackTripDTO> getByPropertyId(@PathVariable("id") UUID propertyId) {
		return feedbackService.getByPropertyId(propertyId);
	}
	
	@GetMapping("/{userId}/{propertyId}")
	public FeedbackTripDTO getByUserIdAndPropertyId(@PathVariable("userId") UUID userId, @PathVariable("propertyId") UUID propertyId) {
	    return feedbackService.getByUserIdAndPropertyId(userId, propertyId);
	}
	
	@PostMapping("/add")
	public FeedbackTripDTO addFeedback(@RequestBody FeedbackTripDTO feedbackDTO) {
		return feedbackService.add(feedbackDTO);
	}

	@PutMapping("/edit/{id}")
	public FeedbackTripDTO editFeedback(@PathVariable("id") UUID feedbackId, @RequestBody FeedbackTripDTO updaFeedbackDTO) {
		return feedbackService.update(feedbackId, updaFeedbackDTO);
	}
	
	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable("id") UUID feedbackId) {
		feedbackService.delete(feedbackId);
	}
	
}
