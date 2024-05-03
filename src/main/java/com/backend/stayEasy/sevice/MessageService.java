package com.backend.stayEasy.sevice;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.stayEasy.entity.ChatRoom;
import com.backend.stayEasy.entity.Message;
import com.backend.stayEasy.entity.Token;
import com.backend.stayEasy.entity.User;
import com.backend.stayEasy.repository.IChatRoomRepository;
import com.backend.stayEasy.repository.IMessageRepository;
import com.backend.stayEasy.repository.TokenRepository;
import com.backend.stayEasy.sevice.impl.IMessageService;


@Service
public class MessageService implements IMessageService {
	@Autowired
	private IMessageRepository messageRepository;
	@Autowired
	private TokenRepository tokenRepository;
	@Autowired
	private IChatRoomRepository chatRoomRepository;
	@Override
	public List<Message> getAllMessages() {
		return messageRepository.findAll();
	}

	@Override
	public Optional<Message> findMessageById (UUID id) {
		Optional<Message> temp = messageRepository.findById(id);
		return temp;
	}

	@Override
	public Message saveMessage(Message message) {
		Message mess = messageRepository.save(message);
		return mess;
	}

	@Override
	public void deleteMessage(UUID id) {
		messageRepository.deleteById(id);

	}

	@Override
	public Optional<Message> firtMessage(String token, UUID roomId) {
		// TODO Auto-generated method stub
		System.out.println(token);
		System.out.println(roomId);
		String[] headerParts = token.split(" ");
		String t = headerParts.length == 2 ? headerParts[1] : null;
		Optional<Token> data = tokenRepository.findByToken(t);
		if (data.isPresent()) {
			Token tokenObject = data.get();
			User user = tokenObject.getUser();
			UUID idUser = user.getId();
			ChatRoom chatroom = chatRoomRepository.findById(roomId).get();
			if (idUser.equals(chatroom.getHostId()) || idUser.equals(chatroom.getUserId())) {
				return messageRepository.findFirstByChatRoomIdOrderByCreateAtDesc(roomId);
			} else {
				return Optional.empty();
			}
		}else {
			return Optional.empty();
		}
		
		
	}

}
