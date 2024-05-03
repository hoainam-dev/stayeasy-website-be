package com.backend.stayEasy.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "property")

public class Property {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "property_id")
	private UUID propertyId;

	@Column(name = "property_name", columnDefinition = "nvarchar(255)")
	private String propertyName;

	@Column(columnDefinition = "ntext")
	private String description;

	@Column(columnDefinition = "nvarchar(255)")
	private String address;
	private int numBedRoom;
	private int numBathRoom;

	@Column(name = "is_null")
	private boolean isNull;
	
	@Column(name = "status")
	private Boolean status;

	@Column(name = "num_guests")
	private int numGuests;

	@Column(name = "discount")
	private int discount;

	@Column(name = "create_at")
	private Date createAt;
	
	@Column (name = "service_fee")
	private int serviceFee;
	

	private String thumbnail;
	private Float price;
	private Float rating;

	@ManyToOne
	private User user;

//	@OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	private Set<Feedback> feedbacks;
	@OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL)

	private List<Like> likes;

	@OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Booking> booking;

	@OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Images> images;

	@OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FeedbackTrip> feedbacks;
	
	@OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<PropertyCategory> propertyCategories;
	
	@OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<PropertyUilitis> propertyUilitis;

	@OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<PropertyRules> propertyRules;

//	@ManyToMany
//	@JoinTable(name = "category_property", joinColumns = @JoinColumn(referencedColumnName = "property_id"), inverseJoinColumns = @JoinColumn(referencedColumnName = "category_id"))
//	private Set<Category> categories = new HashSet<>();
}