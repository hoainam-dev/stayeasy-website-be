package com.backend.stayEasy.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.stayEasy.entity.Message;


public interface IMessageRepository extends JpaRepository<Message, UUID>{
    Optional<Message> findFirstByChatRoomIdOrderByCreateAtDesc(UUID roomId);
}
