package com.backend.stayEasy.api;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stayEasy.dto.TokenDTO;
import com.backend.stayEasy.sevice.impl.ITokenService;

@RestController
@RequestMapping("/api/v1/token")
public class TokenAPI {
	
	@Autowired
	private ITokenService tokenService;
	
	@GetMapping
	public ResponseEntity<List<TokenDTO>> getAllToken(){
		return ResponseEntity.ok(tokenService.getAllToken());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<TokenDTO> getTokenById(@PathVariable String id){
		return ResponseEntity.ok(tokenService.getTokenById(UUID.fromString(id)));
	}
	
}
