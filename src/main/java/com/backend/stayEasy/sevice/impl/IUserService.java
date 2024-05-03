package com.backend.stayEasy.sevice.impl;

import java.util.List;
import java.util.UUID;

import com.backend.stayEasy.dto.UserDTO;

public interface IUserService {
	List<UserDTO> getAllUser();
	
	UserDTO getUserById(UUID id);
	
	UserDTO getUserByEmail(String email);
	
	UserDTO getUserByToken(String token);
	
	UserDTO save(UserDTO userDTO);
}
