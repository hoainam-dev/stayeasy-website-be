package com.backend.stayEasy.convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.backend.stayEasy.dto.RoleDTO;
import com.backend.stayEasy.dto.UserDTO;
import com.backend.stayEasy.entity.Role;
import com.backend.stayEasy.entity.User;
import com.backend.stayEasy.repository.UserRepository;

@Component
public class RoleConverter {

	@Autowired
	private UserConverter userConverter;

	@Autowired
	private UserRepository userRepository;

	public RoleDTO toDTO(Role role) {
		RoleDTO result = new RoleDTO();
		result.setId(role.getId());
		result.setRoleName(role.getRoleName());
		result.setUsers(role.getUsers().stream().map(User -> userConverter.toDTO(User)).collect(Collectors.toList()));

		return result;
	}

	public Role toEntity(RoleDTO roleDTO) {
		Role result = new Role();
		result.setRoleName(roleDTO.getRoleName());
		result.setUsers(roleDTO.getUsers().stream().map(user->userRepository.findById(user.getId()).get()).collect(Collectors.toList()));

		return result;
	}
	
	public Role toEntity(Role role, RoleDTO roleDTO) {
		List<User> users = new ArrayList<>();

		for (UserDTO user : roleDTO.getUsers()) {
			users.add(userRepository.findById(user.getId()).get());
		}

		role.setRoleName(roleDTO.getRoleName());
		role.setUsers(roleDTO.getUsers().stream().map(user->userRepository.findById(user.getId()).get()).collect(Collectors.toList()));

		return role;
	}
}
