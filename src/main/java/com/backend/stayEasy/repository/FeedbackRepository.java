package com.backend.stayEasy.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.stayEasy.entity.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, UUID> {

	List<Feedback> findByPropertyId(UUID propertyId);
	List<Feedback> findByPropertyIdOrderByCreateAtDesc(UUID propertyId);

}
