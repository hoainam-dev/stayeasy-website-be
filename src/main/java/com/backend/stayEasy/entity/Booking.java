package com.backend.stayEasy.entity;

import com.backend.stayEasy.enums.Confirmation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "Booking")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Booking {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID bookingId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "property_id", nullable = false)
	private Property property;
	
	@Column(name = "check-in")
	private Date checkIn;
	
	@Column(name = "check-out")
	private Date checkOut;
	
	@Column(name = "date-booking")
	private Date dateBooking;
	@Column(name = "numNight")
	private int numNight;
	
	@Column(name = "total_price")
    private Double totalPrice;
	
	@Column(name = "status")
	private Boolean status;
	
	@Column(name = "num-guest")
	private int numGuest;
	
	@Column(name = "cancel")
	private Boolean cancel;
	@Enumerated(EnumType.STRING)
	private Confirmation confirmation = Confirmation.RESERVE;
	
	@OneToMany(mappedBy = "booking", cascade = CascadeType.ALL)
    private List<PaymentBill> paymentBills;
}
