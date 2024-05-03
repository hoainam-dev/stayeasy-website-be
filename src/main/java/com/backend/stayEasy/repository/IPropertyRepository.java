package com.backend.stayEasy.repository;


import com.backend.stayEasy.entity.Property;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;



@Repository
public interface IPropertyRepository extends JpaRepository<Property, UUID>{

	Property findByPropertyId(UUID propertyId);
	
	@Query(value = "SELECT * FROM property WHERE (property_name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE %:keySearch% OR address COLLATE SQL_Latin1_General_CP1_CI_AI LIKE %:keySearch%)", nativeQuery = true)
	List<Property> findByPropertyNameOrAddressContainingIgnoreCase(String keySearch);
	
	@Query("SELECT p FROM Property p WHERE p.propertyId NOT IN ( " +
	           "SELECT DISTINCT b.property.propertyId FROM Booking b " +
	           "WHERE b.checkIn <= :endDate AND b.checkOut >= :startDate) " +
	           "AND LOWER(p.address) LIKE LOWER(CONCAT('%', :address, '%'))")
	List<Property> findAvailableProperties(Date startDate, Date endDate, String address);
	
	
	 @Query("SELECT COUNT(p) FROM Property p WHERE p.createAt BETWEEN :startDate AND :endDate")
	    long countPropertiesBetween(Date startDate, Date endDate);
	 // Get Properties of host
	List<Property> findAllByUserId(UUID userId);
	
	Page<Property> findAll(Pageable pageable);
}
