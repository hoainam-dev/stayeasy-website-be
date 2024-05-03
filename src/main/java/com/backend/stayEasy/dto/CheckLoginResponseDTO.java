package com.backend.stayEasy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckLoginResponseDTO {
	private String message;
	private boolean isLogin;
	private boolean isValid;
	private boolean isExist;
	private boolean isExpried;
	private UserDTO user;
}
