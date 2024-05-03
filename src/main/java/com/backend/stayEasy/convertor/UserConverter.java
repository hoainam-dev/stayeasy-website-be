package com.backend.stayEasy.convertor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.backend.stayEasy.dto.UserDTO;
import com.backend.stayEasy.entity.Address;
import com.backend.stayEasy.entity.Role;
import com.backend.stayEasy.entity.User;
import com.backend.stayEasy.repository.AddressRepository;
import com.backend.stayEasy.repository.RoleRepository;

@Component
public class UserConverter {

	@Autowired
	private AddressConverter addressConverter;

	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	public UserDTO toDTO(User user) {
		UserDTO result = new UserDTO();
		
		result.setId(user.getId());
		result.setEmail(user.getEmail());
		result.setFirstName(user.getFirstName());
		result.setLastName(user.getLastName());
		result.setPhone(user.getPhone());
		if (user.getAddress() != null) {
			result.setAddress(addressConverter.toDTO(user.getAddress()));
		}
		result.setRoles(user.getRoles().stream().map(Role -> Role.getRoleName()).collect(Collectors.toList()));
		result.setAvatar(user.getAvatar());
		result.setCreateAt(user.getCreatedAt());
		result.setUpdateAt(user.getUpdatedAt());
		return result;
	}

	public User toEntity(User user, UserDTO userDTO) {
		user.setFirstName(Optional.ofNullable(userDTO.getFirstName()).orElse(user.getFirstName()));
		user.setLastName(Optional.ofNullable(userDTO.getLastName()).orElse(user.getLastName()));
		user.setEmail(Optional.ofNullable(userDTO.getEmail()).orElse(user.getEmail()));
		user.setPhone(Optional.ofNullable(userDTO.getPhone()).orElse(user.getPhone()));
		if (userDTO.getAddress() != null&&user.getAddress() != null) {
			Address oldAddress = addressRepository.findById(user.getAddress().getId()).get();
			addressRepository.save(addressConverter.toEntity(oldAddress, userDTO.getAddress()));
		} else {
			user.setAddress(userDTO.getAddress() != null ? addressConverter.toEntity(userDTO.getAddress()) : user.getAddress());
		}
		user.setAvatar(Optional.ofNullable(userDTO.getAvatar()).orElse(user.getAvatar()));
		if(userDTO.getRoles() != null) {
			// Sử dụng Stream để chuyển đổi từ tên vai trò sang Role Entity
			List<Role> roles = userDTO.getRoles().stream()
	                .map(roleName -> roleRepository.findRoleByName("ROLE_"+roleName))
	                .collect(Collectors.toList());

	        user.setRoles(roles);
		}else {
			user.setRoles(user.getRoles());
		}
		return user;
	}

}