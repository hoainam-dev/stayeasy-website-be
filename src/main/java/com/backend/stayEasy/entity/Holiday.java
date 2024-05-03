package com.backend.stayEasy.entity;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Holiday {


	private String holiday;
	private String startDate;
	private String endDate;
	private int rise;
	
}
