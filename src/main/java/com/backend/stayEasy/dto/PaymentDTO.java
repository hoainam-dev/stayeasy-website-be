package com.backend.stayEasy.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentDTO {
    private UUID paymentBillId ;
    private  String paymentId;
    private BookingDTO bookingDTO ;
    private  UUID bookingId;
    private  String capturesId;
    private String refundStatus;
    private  float amount;
    private String method;
    private String accountType;
    private String status;
    private String createTime;
}