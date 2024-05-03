package com.backend.stayEasy.sevice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.backend.stayEasy.dto.ChatRoomDTO;
import com.backend.stayEasy.dto.HostDTO;
import com.backend.stayEasy.entity.ChatRoom;
import com.backend.stayEasy.entity.Message;
import com.backend.stayEasy.entity.Token;
import com.backend.stayEasy.entity.User;
import com.backend.stayEasy.repository.IChatRoomRepository;
import com.backend.stayEasy.repository.IMessageRepository;
import com.backend.stayEasy.repository.TokenRepository;
import com.backend.stayEasy.repository.UserRepository;
import com.backend.stayEasy.sevice.impl.IChatRoomService;

@Service
public class ChatRoomService implements IChatRoomService {

	@Autowired
	private IChatRoomRepository chatRoomRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private IMessageRepository messageRepository;

	@Autowired
	private TokenRepository tokenRepository;

	@Override
	public List<ChatRoom> getAllChatRoom() {

		return chatRoomRepository.findAll();
	}

	@Override
	public Optional<ChatRoom> findChatRoomById(UUID id) {
		// TODO Auto-generated method stub
		return chatRoomRepository.findById(id);
	}

	@Override
	public ChatRoom saveChatRoom(ChatRoom chatRoom) {
		// TODO Auto-generated method stub
		return chatRoomRepository.save(chatRoom);
	}

	@Override
	public void deleteChatRoom(UUID id) {
		// TODO Auto-generated method stub
		chatRoomRepository.deleteById(id);
	}

	@Override
	public ResponseEntity<List<Message>> getAllMessageChatRoom(UUID id, String token) {
		String[] headerParts = token.split(" ");
		String t = headerParts.length == 2 ? headerParts[1] : null;
		Optional<Token> data = tokenRepository.findByToken(t);
		if (data.isPresent()) {
			// System.out.println(data);
			Token tokenObject = data.get();
			User user = tokenObject.getUser();
			UUID idUser = user.getId();
			ChatRoom chatroom = chatRoomRepository.findById(id).get();
			if (idUser.equals(chatroom.getHostId()) || idUser.equals(chatroom.getUserId())) {
				List<Message> messages = chatRoomRepository.findAllMessagesByChatRoomId(id);
				return new ResponseEntity<>(messages, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.FORBIDDEN);

			}
		} else {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

	}

	@Override
	public List<ChatRoom> findByUserIdOrHostId(UUID userId, UUID hostId) {
		return chatRoomRepository.findByUserIdOrHostId(userId, hostId);
	}

	@Override
	public HostDTO findHostById(UUID id) {
		User user = userRepository.findById(id).get();
		HostDTO host = new HostDTO();
		host.setAvatar(user.getAvatar());
		host.setFirstName(user.getFirstName());
		host.setLastName(user.getLastName());

		return host;
	}

	@Override
	public void addFirstRoom(ChatRoomDTO chatRoom) {
		UUID userId = chatRoom.getUserId();
		UUID hostId = chatRoom.getHostId();
		ChatRoom existingRoom = chatRoomRepository.findByUserIdAndHostId(userId, hostId);
		if (existingRoom == null) {
			existingRoom = chatRoomRepository.findByUserIdAndHostId(hostId, userId);
		}

		if (existingRoom == null) {
			ChatRoom room = new ChatRoom();
			room.setUserId(userId);
			room.setHostId(hostId);
			room.setCreateAt(LocalDateTime.now());
			room.setUpdateAt(LocalDateTime.now());
			chatRoomRepository.save(room);
			Message m = new Message();
			m.setChatRoomId(room.getRoomChatId());
			m.setContent(chatRoom.getContent());
			m.setUserId(chatRoom.getUserId());
			m.setCreateAt(LocalDateTime.now());
			m.setUpdateAt(LocalDateTime.now());
			messageRepository.save(m);
		} else {
			Message m = new Message();
			m.setChatRoomId(existingRoom.getRoomChatId());
			m.setContent(chatRoom.getContent());
			m.setUserId(chatRoom.getUserId());
			m.setCreateAt(LocalDateTime.now());
			m.setUpdateAt(LocalDateTime.now());
			messageRepository.save(m);
		}

	}

	@Override
	public boolean checkRoom(String token, UUID roomId) {
		String[] headerParts = token.split(" ");
		String t = headerParts.length == 2 ? headerParts[1] : null;
		Optional<Token> data = tokenRepository.findByToken(t);

		if (data.isPresent()) {
			// System.out.println(data);
			Token tokenObject = data.get();
			User user = tokenObject.getUser();
			UUID idUser = user.getId();
			ChatRoom chatroom = chatRoomRepository.findById(roomId).get();
			if (idUser.equals(chatroom.getHostId()) || idUser.equals(chatroom.getUserId())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

}
