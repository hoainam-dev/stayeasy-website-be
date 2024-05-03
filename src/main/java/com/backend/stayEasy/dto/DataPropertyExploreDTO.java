package com.backend.stayEasy.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataPropertyExploreDTO {
	private long totalCount;
	private List<PropertyDTO> properties;

}
