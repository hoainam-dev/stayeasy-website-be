package com.backend.stayEasy.sevice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.stayEasy.convertor.UserConverter;
import com.backend.stayEasy.dto.UserDTO;
import com.backend.stayEasy.entity.User;
import com.backend.stayEasy.repository.UserRepository;
import com.backend.stayEasy.sevice.impl.IUserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserConverter userConverter;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * @author NamHH
	 */
	public List<UserDTO> getAllUser() {
		List<UserDTO> result = new ArrayList<>();
		for (User user : userRepository.findAll()) {
			//chuyển user lấy được thành DTO
			result.add(userConverter.toDTO(user));
		}
		return result;
	}

	/**
	 * @author NamHH
	 */
	@Transactional
	public UserDTO getUserById(UUID id) {
		return userConverter.toDTO(userRepository.findById(id).get());
	}

	/**
	 * @author NamHH
	 */
	public UserDTO getUserByEmail(String email) {
		return userConverter.toDTO(userRepository.findByEmail(email).get());
	}

	/**
	 * @author NamHH
	 */
	@Transactional
	public UserDTO getUserByToken(String token) {
        return userConverter.toDTO(userRepository.findByToken(token).orElse(null));
	}

	/**
	 * 
	 * @author NamHH
	 * @param userDTO
	 * @return
	 */
	@Transactional
	public UserDTO save(UserDTO userDTO) {
		User user = new User();

		//check nếu body request có userId thì update
		if (userDTO.getId() != null) {
			//tìm user trong db
			User oldUser = userRepository.findUserById(userDTO.getId()).get();
			//set lại giá trị mới cho user
			user = userConverter.toEntity(oldUser, userDTO);
			//set thời gian update
			user.setUpdatedAt(LocalDateTime.now());
		}
		//set thời gian create
		user.setCreatedAt(LocalDateTime.now());
		return userConverter.toDTO(userRepository.save(user));
	}
	
	/**
	 * 
	 * @author NamHH
	 * @param userDTO
	 * @return
	 */
	@Transactional
	public UserDTO resetPassword(String email, String password) {
		User user = new User();
		//check nếu body request có userId thì update
		//tìm user trong db
		user = userRepository.findByEmail(email).get();
		//set lại giá trị mới cho user
		user.setPassword(passwordEncoder.encode(password));
		//set thời gian update
		user.setUpdatedAt(LocalDateTime.now());
		
		return userConverter.toDTO(userRepository.save(user));
	}

}