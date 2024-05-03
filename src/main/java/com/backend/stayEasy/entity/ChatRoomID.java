package com.backend.stayEasy.entity;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomID implements Serializable {
	private static final long serialVersionUID = 1L;

	private UUID userId;
	private UUID hostId;
}
