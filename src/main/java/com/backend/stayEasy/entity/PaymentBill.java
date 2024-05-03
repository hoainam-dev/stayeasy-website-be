package com.backend.stayEasy.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
@Table(name = "payment-bill")
public class PaymentBill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID paymentBillId;
    @Column(name = "payment-id")
    private String paymentId;
    @Column(name = "amount", nullable = false)
    private Float amount;
    @Column(name = "method", nullable = false)
    private String method;
    @Column(name = "status")
    private String status;
    @Column(name = "create_time")
    private String createTime;
    @Column(name = "account_type")
    private String accountType;
    @Column (name = "captures")
    private String captures;
    @Column (name = "refund_status")
    private String refundStatus;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

}
