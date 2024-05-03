package com.backend.stayEasy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {
    // TO CONVERTER WITH ENTITY
    private UserDTO userDTOS;
    private UUID userId;
    private  PropertyDTO propertyDTOS;
    private Date checkIn;
    private String confirmation;
    // TO CREATE BOOKING AND PAYMENT
    private UUID bookingId ;
    private UUID propertyId;
    private Double total;
    private  Boolean status;
    private String propertyName;
    private  int numOfGuest;
    private Date checkOut;
    private Date dateBooking;
    private  int numberNight;
    private Double price;
    private  String currency;
    private String method;
    private String intent;
    private String description ;
}