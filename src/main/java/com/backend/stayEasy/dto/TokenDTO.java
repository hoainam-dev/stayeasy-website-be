package com.backend.stayEasy.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO {
	private UUID id;
	private boolean expired;
	private boolean revoked;
	private String token;
	private String refreshToken;
	private String type;
	private LocalDateTime expirationToken;
	private LocalDateTime expirationRefreshToken;
	private UUID userId;
}
