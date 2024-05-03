package com.backend.stayEasy.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Address {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
	private UUID id;
	
	@Column(name = "street", columnDefinition = "NVARCHAR(255)")
	private String street;
	
	@Column(name = "ward", columnDefinition = "NVARCHAR(255)")
	private String ward;
	
	@Column(name = "district", columnDefinition = "NVARCHAR(255)")
	private String district;
	
	@Column(name = "city", columnDefinition = "NVARCHAR(255)")
	private String city;
	
	@Column(name = "country", columnDefinition = "NVARCHAR(255)")
	private String country;
	
	@OneToOne(mappedBy = "address")
    private User user;
}
