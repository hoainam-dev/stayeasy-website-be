package com.backend.stayEasy.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.backend.stayEasy.entity.Token;

public interface TokenRepository extends JpaRepository<Token, UUID> {

	@Query(value = """
			select t from Token t inner join User u\s
			on t.user.id = u.id\s
			where u.id = :id and (t.expired = false or t.revoked = false)\s
			""")
	List<Token> findAllValidTokenByUser(UUID id);
	
	@Modifying
    @Transactional
    @Query("UPDATE Token t SET t.expired = true, t.revoked = true WHERE t.expirationTokenDate <= :now AND t.expired = false")
    void updateExpiredTokens(@Param("now") LocalDateTime now);

	Optional<Token> findByToken(String token);
	
	Optional<Token> findByRefreshToken(String refreshToken);
}
