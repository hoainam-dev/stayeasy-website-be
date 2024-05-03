package com.backend.stayEasy.repository;

import java.sql.Date;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.stayEasy.entity.Statistics;

public interface StatisticsRepository extends JpaRepository<Statistics, UUID> {
	@Query("SELECT s FROM Statistics s WHERE s.date BETWEEN :startDate AND :endDate")
    List<Statistics> sumRevenueFromStartOfYearToDate(Date startDate, Date endDate);
}
