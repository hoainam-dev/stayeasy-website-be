package com.backend.stayEasy.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.stayEasy.entity.PropertyRules;

@Repository
public interface PropertyRulesRepository extends JpaRepository<PropertyRules, UUID>{

}
