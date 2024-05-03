package com.backend.stayEasy.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private List<String> role;
}
