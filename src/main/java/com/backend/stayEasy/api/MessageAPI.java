package com.backend.stayEasy.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stayEasy.entity.Message;
import com.backend.stayEasy.sevice.impl.IMessageService;

import jakarta.servlet.http.HttpServletRequest;


@CrossOrigin
@RestController
@RequestMapping(value="/api/v1/stayeasy/message", produces = "application/json")
public class MessageAPI {
	@Autowired
	private IMessageService messageService;
	
	
	@GetMapping("/get")
	public List<Message> getAllMessage() {
		return messageService.getAllMessages();
	}
	
	@GetMapping("/get/{id}")
	public Optional<Message> getMessage(@PathVariable("id") UUID id) {
		return messageService.findMessageById(id);
	}
	
	@PostMapping("/add")
	public Message addMessage(@RequestBody Message message) {
		message.setCreateAt(LocalDateTime.now());
		message.setUpdateAt(LocalDateTime.now());
		return messageService.saveMessage(message);
	}
	
	@PutMapping("/update/{id}")
	public Message updateMessage(@PathVariable("id") UUID id, @RequestBody Message message) {
		message.setMessageId(id);
		Optional<Message> mess = messageService.findMessageById(id);
		mess.ifPresent(m -> {
			message.setChatRoomId(m.getChatRoomId());
			message.setUserId(m.getUserId());
			message.setCreateAt(m.getCreateAt());
			message.setUpdateAt(LocalDateTime.now());
		});
		return messageService.saveMessage(message);
		
		
	}
	
	
	@DeleteMapping("/delete/{id}")
	public void deleteMessage(@PathVariable("id") UUID id) {
		messageService.deleteMessage(id);
	}

	@GetMapping("/get-first/{roomId}")
	public Optional<Message> firtMessage(@PathVariable("roomId") UUID roomId, HttpServletRequest request) {
		return messageService.firtMessage(request.getHeader("Authorization"),roomId);
	}
}
