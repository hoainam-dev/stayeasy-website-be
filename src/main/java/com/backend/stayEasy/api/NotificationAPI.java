package com.backend.stayEasy.api;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stayEasy.entity.Notification;
import com.backend.stayEasy.sevice.impl.INotificationService;

import jakarta.servlet.http.HttpServletRequest;


@CrossOrigin
@RestController
@RequestMapping(value="/api/v1/stayeasy/notification", produces = "application/json")
public class NotificationAPI {
    @Autowired
	private INotificationService notificationService;

	@PostMapping("/add")
	public Notification addMessage(@RequestBody Notification noti) {
		noti.setCreateAt(LocalDateTime.now());
		noti.setUpdateAt(LocalDateTime.now());
		return notificationService.saveNotification(noti);
	}

    @GetMapping("/get")
	public List<Notification> getAllNotification() {
		return notificationService.getAllNotification();
	}
    
    @GetMapping("/user/get")
	public List<Notification> getAllNotificationForUser(HttpServletRequest request) {
		return notificationService.getAllNotificationForUser(request.getHeader("Authorization"));
	}
}
