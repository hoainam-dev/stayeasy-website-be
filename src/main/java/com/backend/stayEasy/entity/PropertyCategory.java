package com.backend.stayEasy.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "property_category")
public class PropertyCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "property_category_id")
	private UUID propertyCategoryId;

	@ManyToOne()
	private Property property;

	@ManyToOne()
	private Category category;
}