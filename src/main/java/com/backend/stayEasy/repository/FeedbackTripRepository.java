package com.backend.stayEasy.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.stayEasy.entity.FeedbackTrip;

@Repository
public interface FeedbackTripRepository extends JpaRepository<FeedbackTrip, UUID>{
	List<FeedbackTrip> findByPropertyPropertyId(UUID propertyId);
	FeedbackTrip findByPropertyPropertyIdAndUserId(UUID propertyId, UUID userId);
}
