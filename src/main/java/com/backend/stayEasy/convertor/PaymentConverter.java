package com.backend.stayEasy.convertor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.backend.stayEasy.dto.PaymentDTO;
import com.backend.stayEasy.entity.PaymentBill;
import com.backend.stayEasy.repository.BookingRepository;

@Component
public class PaymentConverter {
	@Autowired
	private BookingConverter bookingConverter;

	@Autowired
	private BookingRepository bookingRepository;

	public PaymentDTO toDTO(PaymentBill paymentBill) {
		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setPaymentBillId(paymentBill.getPaymentBillId());
		paymentDTO.setAmount(paymentBill.getAmount());
		paymentDTO.setBookingDTO(bookingConverter.toDTO(bookingRepository.findById(paymentBill.getBooking().getBookingId()).get()));
		paymentDTO.setAccountType(paymentBill.getAccountType());
		paymentDTO.setPaymentId(paymentBill.getPaymentId());
		paymentDTO.setMethod(paymentBill.getMethod());
		paymentDTO.setCapturesId(paymentBill.getCaptures());
		paymentDTO.setRefundStatus(paymentBill.getRefundStatus());
		paymentDTO.setCreateTime(paymentBill.getCreateTime());
		paymentDTO.setStatus(paymentBill.getStatus());
		return paymentDTO;
	}

	public PaymentBill toEntity(PaymentDTO paymentDTO) {
		PaymentBill paymentBill = new PaymentBill();
		paymentBill.setPaymentBillId(paymentDTO.getPaymentBillId());
		paymentBill.setAmount(paymentDTO.getAmount());
		paymentBill.setMethod(paymentDTO.getMethod());
		paymentBill.setBooking(bookingRepository.findById(paymentDTO.getBookingId()).get());
		paymentBill.setAccountType(paymentDTO.getAccountType());
		paymentBill.setCreateTime(paymentDTO.getCreateTime());
		paymentBill.setPaymentId(paymentDTO.getPaymentId());
		paymentBill.setStatus(paymentDTO.getStatus());
		paymentBill.setCaptures(paymentDTO.getCapturesId());
		paymentBill.setRefundStatus(paymentDTO.getRefundStatus());
		return paymentBill;
	}
}
