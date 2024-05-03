package com.backend.stayEasy.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.stayEasy.entity.Notification;

public interface INotificationRepository extends JpaRepository<Notification, UUID> {
	List<Notification> findByReceiverId(UUID receiverId);
}
