package com.backend.stayEasy.convertor;

import org.springframework.stereotype.Component;

import com.backend.stayEasy.dto.StatisticsDTO;
import com.backend.stayEasy.entity.Statistics;

@Component
public class StatisticsConverter {

	public StatisticsDTO toDTO(Statistics statistics) {
		StatisticsDTO statisticsDTO = new StatisticsDTO();
		
		statisticsDTO.setStatisticsId(statistics.getStatisticsId());
		statisticsDTO.setDate(statistics.getDate() );
		statisticsDTO.setRevenue(statistics.getRevenue()!= 0?statistics.getRevenue(): 0 );
		statisticsDTO.setTotalAccount(statistics.getTotalAccount()!= null?statistics.getTotalAccount(): 0 );
		statisticsDTO.setTotalBookings(statistics.getTotalBookings()!= null?statistics.getTotalBookings(): 0 );
		statisticsDTO.setTotalPost(statistics.getTotalPost()!= null?statistics.getTotalPost(): 0 );
		statisticsDTO.setTotalCancelBooking(statistics.getTotalCancelBooking()!= null?statistics.getTotalCancelBooking(): 0 );
		statisticsDTO.setTotalLike(statistics.getTotalLike() != null?statistics.getTotalLike(): 0 );
		
		return statisticsDTO;
	}
}
