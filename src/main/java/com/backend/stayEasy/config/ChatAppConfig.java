package com.backend.stayEasy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class ChatAppConfig implements WebSocketMessageBrokerConfigurer{
	 
	@Override
	public void registerStompEndpoints(StompEndpointRegistry config) { 
		// TODO Auto-generated method stub
		config.addEndpoint("/api/v1/stayeasy/ws").setAllowedOriginPatterns("*").withSockJS();
	}
	
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// TODO Auto-generated method stub
		config.enableSimpleBroker("/api/v1/stayeasy/topic","/api/v1/stayeasy/notification");
		config.setApplicationDestinationPrefixes("/api/v1/stayeasy/app");
	}

	
}
