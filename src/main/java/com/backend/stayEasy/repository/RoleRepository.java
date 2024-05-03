package com.backend.stayEasy.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.backend.stayEasy.entity.Role;

public interface RoleRepository extends JpaRepository<Role, UUID> {
	
    @Query("SELECT r FROM Role r WHERE r.roleName = :roleName")
	Role findRoleByName(String roleName);
}
