package com.backend.stayEasy.sevice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.stayEasy.entity.Notification;
import com.backend.stayEasy.entity.Token;
import com.backend.stayEasy.entity.User;
import com.backend.stayEasy.repository.INotificationRepository;
import com.backend.stayEasy.repository.TokenRepository;
import com.backend.stayEasy.sevice.impl.INotificationService;

@Service
public class NotificationService implements INotificationService {
	@Autowired
	private INotificationRepository notificationRepository;
	@Autowired
	private TokenRepository tokenRepository;

	@Override
	public Notification saveNotification(Notification notification) {
		Notification noti = notificationRepository.save(notification);
		return noti;
	}

	@Override
	public List<Notification> getAllNotification() {
		return notificationRepository.findAll();
	}

	@Override
	public List<Notification> getAllNotificationForUser(String token) {
		System.out.println(token);
		String[] headerParts = token.split(" ");
		String t = headerParts.length == 2 ? headerParts[1] : null;
		Optional<Token> data = tokenRepository.findByToken(t);
		if (data.isPresent()) {
			Token tokenObject = data.get();
			User user = tokenObject.getUser();
			UUID idUser = user.getId();
			List<Notification> list = notificationRepository.findByReceiverId(idUser);
			return list;
		} else {
			return null;
		}
	}

}
