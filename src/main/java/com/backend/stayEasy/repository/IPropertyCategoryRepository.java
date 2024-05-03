package com.backend.stayEasy.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.stayEasy.entity.PropertyCategory;

@Repository
public interface IPropertyCategoryRepository extends JpaRepository<PropertyCategory, UUID>{
	
	List<PropertyCategory> findByCategoryCategoryId(UUID categoryId);
}
