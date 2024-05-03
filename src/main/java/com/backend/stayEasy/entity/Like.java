package com.backend.stayEasy.entity;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "[Like]")
public class Like {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID likeId;

	@ManyToOne()
	private User user;

	@ManyToOne()
	private Property property;

}