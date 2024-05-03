package com.backend.stayEasy.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.stayEasy.entity.Images;


@Repository
public interface IImageRepository extends JpaRepository<Images, UUID>{
	List<Images> findByPropertyPropertyId(UUID propertyId);
	Images findByUrl(String url);
}