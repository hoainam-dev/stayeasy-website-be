package com.backend.stayEasy.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "rules")
public class Rules {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID rulesId;

	@Column(name = "rules_name", columnDefinition = "nvarchar(255)")
	private String rulesName;

	@Column(name = "rules_type", columnDefinition = "nvarchar(255)")
	private String type;

	@OneToMany(mappedBy = "rules", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<PropertyRules> propertyRules;
}
