package com.backend.stayEasy.dto;

import java.util.UUID;

import lombok.Data;

@Data
public class AddressDTO {
	private UUID id;
	private String street;
	private String ward;
	private String district;
	private String city;
	private String country;
}
