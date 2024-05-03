package com.backend.stayEasy.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	private UUID id;
	private String email;
	private String firstName;
	private String lastName;
	private String phone;
	private AddressDTO address;
	private String avatar;
	private List<String> roles;
	private LocalDateTime updateAt;
	private LocalDateTime createAt;
}
