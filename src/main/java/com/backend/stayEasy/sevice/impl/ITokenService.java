package com.backend.stayEasy.sevice.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.backend.stayEasy.dto.TokenDTO;
import com.backend.stayEasy.entity.Token;

public interface ITokenService {
	List<TokenDTO> getAllToken();

	TokenDTO getTokenById(UUID id);

	Optional<Token> getByToken(String token);

	Optional<Token> getByRefreshToken(String refreshToken);

	Token verifyExpiration(Token token);
}
