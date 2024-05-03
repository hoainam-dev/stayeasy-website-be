package com.backend.stayEasy.dto;

import lombok.Data;

@Data
public class PayoutDTO {
    private String sender_batch_id;
    private String email_subject;
    private String email_message;
    private float amount;
    private String note;
    private String sender_item_id;
    private String receiver;
    private String recipient_wallet;
}
