package com.backend.stayEasy.sevice;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import com.backend.stayEasy.repository.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

	private final TokenRepository tokenRepository;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		final String authHeader = request.getHeader("Authorization");
		final String jwt;
		System.out.println(authHeader);
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		jwt = authHeader.substring(7);
		var storedToken = tokenRepository.findByToken(jwt).orElse(null);
		if (storedToken != null) {
			storedToken.setExpired(true);
			storedToken.setRevoked(true);
			tokenRepository.save(storedToken);
			
			// Trả về phản hồi thành công
			Map<String, Object> succcessResponse = new HashMap<>();
			succcessResponse.put("message", "Đăng xuất thành công!");
			succcessResponse.put("status", HttpStatus.OK.value());

	        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	        response.setCharacterEncoding("UTF-8");
	        response.setStatus(HttpStatus.OK.value());
	        
	        try {
	            ObjectMapper objectMapper = new ObjectMapper();
	            String jsonResponse = objectMapper.writeValueAsString(succcessResponse);
	            response.getWriter().write(jsonResponse);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	}
}