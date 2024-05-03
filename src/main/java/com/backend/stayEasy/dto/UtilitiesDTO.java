package com.backend.stayEasy.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class UtilitiesDTO {
	private UUID utilitiId;
	private String UtilitiesName;
	private String type;
}
