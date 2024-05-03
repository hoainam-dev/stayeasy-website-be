package com.backend.stayEasy.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.stayEasy.entity.Like;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {

	List<Like> findByPropertyPropertyId(UUID propertyId);

	void deleteByPropertyPropertyIdAndUserId(UUID idPost, UUID idUser);

	@Query("SELECT COUNT(l) FROM Like l WHERE l.property.propertyId = :propertyId")
    long countByPropertyId(@Param("propertyId") UUID propertyId);
	
}
