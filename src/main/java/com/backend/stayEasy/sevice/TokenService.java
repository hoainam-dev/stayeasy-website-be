package com.backend.stayEasy.sevice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.stayEasy.convertor.TokenConverters;
import com.backend.stayEasy.dto.TokenDTO;
import com.backend.stayEasy.entity.Token;
import com.backend.stayEasy.exception.TokenExceptionHandle;
import com.backend.stayEasy.repository.TokenRepository;
import com.backend.stayEasy.sevice.impl.ITokenService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService implements ITokenService{
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Autowired
	private TokenConverters tokenConverter;
	
	/**
	 * 
	 * @author NamHH
	 * @return
	 */
	public List<TokenDTO> getAllToken(){
		List<TokenDTO> result = new ArrayList<>();
		
		for(Token token:tokenRepository.findAll()) {
			result.add(tokenConverter.toDTO(token));
		}
		
		return result;
	}
	
	public TokenDTO getTokenById(UUID id) {
		return tokenConverter.toDTO(tokenRepository.findById(id).get());
	}
	
	public Optional<Token> getByToken(String token) {
        return tokenRepository.findByToken(token);
    }
	
	
	public Optional<Token> getByRefreshToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken);
    }
	
	
 	@Scheduled(fixedRate = 3600000) // Kiểm tra mỗi giờ
    @Transactional
    public void updateTokenExpirations() {
        LocalDateTime now = LocalDateTime.now();
        tokenRepository.updateExpiredTokens(now);
    }
	
	public Token verifyExpiration(Token token) {
        if (token.getExpirationRefTokenDate().compareTo(LocalDateTime.now()) < 0) {
        	tokenRepository.delete(token);
            throw new TokenExceptionHandle(token.getToken(), "Refresh token Đã hết hạn. Vui lòng đăng nhập lại");
        }

        return token;
    }
}
