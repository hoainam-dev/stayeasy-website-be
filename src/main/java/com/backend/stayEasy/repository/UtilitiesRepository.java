package com.backend.stayEasy.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.stayEasy.entity.Utilities;


@Repository
public interface UtilitiesRepository extends JpaRepository<Utilities, UUID>{

}
