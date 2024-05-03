package com.backend.stayEasy.dto;

import java.sql.Date;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class DailyRevenueDTO {
 
    private Date date;
    private Double revenue;
    
    public DailyRevenueDTO(Date date, Double revenue) {
        this.date = date;	
        this.revenue = revenue;
    }
    
}
