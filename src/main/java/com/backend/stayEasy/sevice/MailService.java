package com.backend.stayEasy.sevice;

import com.backend.stayEasy.dto.BookingDTO;
import com.backend.stayEasy.entity.Mail;
import com.backend.stayEasy.sevice.impl.IMailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class MailService implements IMailService {
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private TemplateEngine templateEngine;

	@Override
	public void sendBook(Mail mail) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
		Context context = new Context();
		context.setVariable("username", "An");
		String htmlContent = templateEngine.process("email_book", context);
		try {
			helper.setText(htmlContent, true);
			helper.setTo(mail.getRecipient());
			helper.setSubject(mail.getSubject());
			mailSender.send(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void sendCancel(Mail mail , BookingDTO bookingDTO , String link) {
		LocalDateTime cancelDate = LocalDateTime.now();
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
		Context context = new Context();
		context.setVariable("bookingDTO", bookingDTO);
		context.setVariable("cancelDate", cancelDate);
		String htmlContent = templateEngine.process("email_cancel", context);
		try {
			helper.setText(htmlContent, true);
			helper.setTo(mail.getRecipient());
			helper.setSubject(mail.getSubject());
			mailSender.send(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	public void sendEmailPayment(Mail mail, BookingDTO bookingDTO, String paymentLink) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
		Context context = new Context();
		context.setVariable("bookingDTO", bookingDTO);
		context.setVariable("paymentLink", paymentLink);
		String htmlContent = templateEngine.process("email-payment", context);
		try {
			helper.setText(htmlContent, true);
			helper.setTo(mail.getRecipient());
			helper.setSubject(mail.getSubject());
			mailSender.send(message);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	@Override
	public void sendEmailVerify(Mail mail, String code) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
		Context context = new Context();
		context.setVariable("code", code);
		String htmlContent = templateEngine.process("email-verify", context);
		try {
			helper.setText(htmlContent, true);
			helper.setTo(mail.getRecipient());
			helper.setSubject(mail.getSubject());
			mailSender.send(message);
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	@Override
	public void sendEmailSuccess(Mail mail, BookingDTO bookingDTO) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, StandardCharsets.UTF_8.name());
		String bookingId = String.valueOf(bookingDTO.getBookingId());
		String lastFiveChars = bookingId.length() > 5 ? bookingId.substring(bookingId.length() - 5).toUpperCase() : bookingId.toUpperCase();
		Context context = new Context();
		context.setVariable("bookingDTO", bookingDTO);
		context.setVariable("bookingId" , lastFiveChars);
		String htmlContent = templateEngine.process("email-success", context);
		try{
			helper.setText(htmlContent, true);
			helper.setTo(mail.getRecipient());
			helper.setSubject(mail.getSubject());
			mailSender.send(message);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
