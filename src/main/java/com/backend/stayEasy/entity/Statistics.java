package com.backend.stayEasy.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="[statistics]")
public class Statistics {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
		private UUID statisticsId;
		private Date date;
		private Double revenue;
		private Long totalAccount;
		private Long totalBookings;
		private Long totalPost;
		private Long totalCancelBooking;
		@Column(name = "total_like", columnDefinition = "bigint default 0")
		private Long totalLike;
		
		

}
