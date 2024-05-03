package com.backend.stayEasy.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePassword {

	private String currentPassword;
	private String newPassword;
	private String confirmationPassword;
}