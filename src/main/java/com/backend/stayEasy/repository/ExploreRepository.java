package com.backend.stayEasy.repository;

import java.util.List;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.stayEasy.entity.Property;

@Repository
public interface ExploreRepository extends JpaRepository<Property, UUID> {

    List<Property> findAll();
    @Query(value = "SELECT * FROM property WHERE (property_name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE %:keySearch% OR description COLLATE SQL_Latin1_General_CP1_CI_AI LIKE %:keySearch%)", nativeQuery = true)
    Page<Property> findByPropertyNameOrDescriptionContainingIgnoreCase(String keySearch, Pageable pageable);

    long count();
    
    @Query(value = "SELECT TOP 5 * FROM property WHERE (property_name COLLATE SQL_Latin1_General_CP1_CI_AI LIKE %:keySearch% OR description COLLATE SQL_Latin1_General_CP1_CI_AI LIKE %:keySearch%) ORDER BY rating DESC", nativeQuery = true)
    List<Property> findByPropertyNameOrDescriptionContainingIgnoreCaseOrderByRatingDesc(String keySearch);

    @Query(value = "SELECT TOP 5 * FROM property WHERE (address COLLATE SQL_Latin1_General_CP1_CI_AI LIKE %:address%) ORDER BY rating DESC", nativeQuery = true)
    List<Property> findByAddressContainingIgnoreCaseOrderByRatingDesc(String address);

}
