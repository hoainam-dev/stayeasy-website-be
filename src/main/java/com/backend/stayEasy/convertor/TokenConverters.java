package com.backend.stayEasy.convertor;

import org.springframework.stereotype.Component;

import com.backend.stayEasy.dto.TokenDTO;
import com.backend.stayEasy.entity.Token;

@Component
public class TokenConverters {
	
	public TokenDTO toDTO(Token token) {
		TokenDTO result = new TokenDTO();
		result.setId(token.getId());
		result.setExpired(token.isExpired());
		result.setRevoked(token.isRevoked());
		result.setToken(token.getToken());
		result.setRefreshToken(token.getRefreshToken());
		result.setExpirationToken(token.getExpirationTokenDate());
		result.setExpirationRefreshToken(token.getExpirationRefTokenDate());
		result.setType(token.getTokenType().toString());
		result.setUserId(token.getUser().getId());
		return result;
	}
}
