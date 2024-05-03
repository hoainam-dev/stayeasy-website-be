package com.backend.stayEasy.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.stayEasy.enums.TokenType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Token {

	@Id
	@Column(name = "id", unique = true, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(unique = true)
	public String token;
	
	@Column(name = "refresh_token", unique = true)
	public String refreshToken;
	
	@Column(name = "expiration_token", unique = true)
	public LocalDateTime expirationTokenDate;
	
	@Column(name = "expiration_refeshtoken", unique = true)
	public LocalDateTime expirationRefTokenDate;
	
	@Column(name = "revoked")
	public boolean revoked;
	
	@Column(name = "expired")
	public boolean expired;

	@Enumerated(EnumType.STRING)
	public TokenType tokenType = TokenType.BEARER;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	public User user;
}
