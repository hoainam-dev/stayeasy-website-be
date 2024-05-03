package com.backend.stayEasy.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImagesDTO {
	private UUID imageId;
	private String url;
	private String description;
	private UUID propertyId;
}
