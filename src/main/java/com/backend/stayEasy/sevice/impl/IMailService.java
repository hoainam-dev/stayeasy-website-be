package com.backend.stayEasy.sevice.impl;

import com.backend.stayEasy.dto.BookingDTO;
import com.backend.stayEasy.entity.Mail;

public interface IMailService {
	void sendBook(Mail mail);

	void sendCancel(Mail mail, BookingDTO bookingDTO, String link);

	void sendEmailPayment(Mail mail, BookingDTO bookingDTO, String paymentLink);

	void sendEmailSuccess(Mail mail, BookingDTO bookingDTO);

	void sendEmailVerify(Mail mail, String Code);

}
