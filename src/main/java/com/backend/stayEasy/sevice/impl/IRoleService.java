package com.backend.stayEasy.sevice.impl;

import com.backend.stayEasy.dto.RoleDTO;

public interface IRoleService {
	RoleDTO save(RoleDTO roleDTO);
	
	RoleDTO getRoleByName(String roleName);
}
