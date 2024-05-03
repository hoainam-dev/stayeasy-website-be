package com.backend.stayEasy.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RefundDTO {
    private String captureId;
    private double refundAmount;
    private String currencyCode;
    // mã paymentBill
    private UUID paypalRequestId;
    // ma booking
    private UUID invoiceId;
    private String noteToPayer;


}
