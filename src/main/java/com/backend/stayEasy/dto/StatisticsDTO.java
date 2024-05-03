package com.backend.stayEasy.dto;

import java.sql.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDTO {
	private UUID statisticsId;
	private Date date;
	private Double revenue;
	private long totalAccount;
	private long totalBookings;
	private long totalPost;
	private long totalCancelBooking;
	private long totalLike;

}
