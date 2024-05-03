package com.backend.stayEasy.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.stayEasy.entity.ChatRoom;
import com.backend.stayEasy.entity.Message;

public interface IChatRoomRepository extends JpaRepository<ChatRoom, UUID> {
	@Query("SELECT m FROM Message m WHERE m.chatRoomId = :chatRoomId ORDER BY m.createAt ASC")
	List<Message> findAllMessagesByChatRoomId(@Param("chatRoomId") UUID chatRoomId);

	List<ChatRoom> findByUserIdOrHostId(UUID userId, UUID hostId);

	ChatRoom findByUserIdAndHostId(UUID userId, UUID hostId);

}
