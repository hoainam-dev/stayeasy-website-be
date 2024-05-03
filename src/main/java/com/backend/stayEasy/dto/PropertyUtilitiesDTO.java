package com.backend.stayEasy.dto;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropertyUtilitiesDTO {
	private UUID utilitiesId;
	private String type;
    private String utilitiesName;
   
}
