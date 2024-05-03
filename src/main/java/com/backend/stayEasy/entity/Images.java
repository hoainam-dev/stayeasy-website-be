package com.backend.stayEasy.entity;

import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="images")
public class Images {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID imageId;
	
	@Column(name = "url", length = Integer.MAX_VALUE)
	private String url;
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Property property;
	
	
	
	public Images(String url, String description, Property property) {
		this.url = url;
		this.description = description;
		this.property = property;
	}



	public Images() {
		super();
		// TODO Auto-generated constructor stub
	}
}