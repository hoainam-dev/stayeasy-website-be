package com.backend.stayEasy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "property_uilitis")
public class PropertyUilitis {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID propertyUtilitiesId;

	@ManyToOne()
	private Property property;

	@ManyToOne()
	private Utilities utilities;

}