package com.backend.stayEasy.sevice;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.backend.stayEasy.convertor.RoleConverter;
import com.backend.stayEasy.dto.RoleDTO;
import com.backend.stayEasy.entity.Role;
import com.backend.stayEasy.repository.RoleRepository;
import com.backend.stayEasy.sevice.impl.IRoleService;

public class RoleService implements IRoleService{
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private RoleConverter roleConverter;

	@Override
	public RoleDTO save(RoleDTO roleDTO) {
		Role role = new Role();
		if(roleDTO.getId() != null) {
			Role oldRole = roleRepository.findById(roleDTO.getId()).get();
			oldRole.setUpdateAt(LocalDateTime.now());
			role = roleConverter.toEntity(oldRole, roleDTO);
		}
		role.setCreateAt(LocalDateTime.now());
		return roleConverter.toDTO(roleRepository.save(role));
	}
	
	@Override
	public RoleDTO getRoleByName(String roleName) {
		return roleConverter.toDTO(roleRepository.findRoleByName(roleName));
	}

}
