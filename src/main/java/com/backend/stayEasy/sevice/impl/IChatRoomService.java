package com.backend.stayEasy.sevice.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.backend.stayEasy.dto.ChatRoomDTO;
import com.backend.stayEasy.dto.HostDTO;
import com.backend.stayEasy.entity.ChatRoom;
import com.backend.stayEasy.entity.Message;

public interface IChatRoomService {
	List<ChatRoom> getAllChatRoom();
	Optional<ChatRoom> findChatRoomById(UUID id);
	ChatRoom saveChatRoom(ChatRoom chatRoom);
	void deleteChatRoom(UUID id);
	ResponseEntity<List<Message>> getAllMessageChatRoom(UUID id, String token);
	
	List<ChatRoom> findByUserIdOrHostId(UUID userId, UUID hostId);

	HostDTO findHostById(UUID id);
	void addFirstRoom(ChatRoomDTO chatRoom);

	boolean checkRoom(String token, UUID roomId);
}
