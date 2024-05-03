package com.backend.stayEasy.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stayEasy.entity.Mail;
import com.backend.stayEasy.sevice.impl.IMailService;


@CrossOrigin
@RestController
@RequestMapping(value="/api/v1/stayeasy/mail", produces = "application/json")
public class MailAPI {
	
	
	@Autowired
	private IMailService mailService;
	
	@PostMapping("/send/book")
	public void SendBookMail(@RequestBody Mail mail) {
		mailService.sendBook(mail);
	}

//	@PostMapping("/send/cancel")
//	public void SendCancleMail(@RequestBody Mail mail) {
//		mailService.sendCancel(mail);
//	}
}
