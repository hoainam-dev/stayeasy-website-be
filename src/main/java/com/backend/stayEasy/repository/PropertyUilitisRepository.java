package com.backend.stayEasy.repository;

import com.backend.stayEasy.entity.PropertyUilitis;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;



public interface PropertyUilitisRepository  extends JpaRepository<PropertyUilitis, UUID>{
	List<PropertyUilitis> findByPropertyPropertyId(UUID propertyId);
	
	List<PropertyUilitis> findAll();
}
