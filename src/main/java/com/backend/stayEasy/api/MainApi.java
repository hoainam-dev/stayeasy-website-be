package com.backend.stayEasy.api;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stayEasy.convertor.LikeConverter;
import com.backend.stayEasy.dto.LikeRequestDTO;
import com.backend.stayEasy.repository.LikeRepository;

import jakarta.transaction.Transactional;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/stayeasy")
public class MainApi {
	
	@Autowired
	private LikeRepository likeRepository;
	
	@Autowired
	private LikeConverter likeConverter;
	
	@GetMapping
	public String homePage() {
		return "WELCOME TO STAYEASY.";
	}
	
	@Transactional
	@PostMapping("/like")
    public ResponseEntity<String> handleLike(@RequestBody LikeRequestDTO likeRequest) {
//		viêt hàm jpa để lưu vào bảng like
		System.out.println("idPost as: " + likeRequest.getIdPost());
		System.out.println("idUser as: " + likeRequest.getIdUser());
		likeRepository.save(likeConverter.convertToLikeEntity(likeRequest));
		return ResponseEntity.ok("Like: " + likeRequest.getIdPost());
    }
	
	
	@Transactional
	 @DeleteMapping("/unlike")
	    public ResponseEntity<String> unlikeProperty(@RequestParam UUID idPost, @RequestParam UUID idUser) {
			System.out.println("idPost: " + idPost);
			System.out.println("idUser: " + idUser);
	        likeRepository.deleteByPropertyPropertyIdAndUserId(idPost, idUser);
	        return ResponseEntity.ok("unLike: " + idPost);
	    }
}