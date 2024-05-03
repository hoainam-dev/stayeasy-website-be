package com.backend.stayEasy.convertor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.backend.stayEasy.dto.LikeRequestDTO;
import com.backend.stayEasy.entity.Like;
import com.backend.stayEasy.entity.Property;
import com.backend.stayEasy.entity.User;

@Component
public class LikeConverter {

	public Like convertToLikeEntity(LikeRequestDTO likeRequestDTO) {
		Like newLike = new Like();
		User user = new User();
		user.setId(likeRequestDTO.getIdUser());
		Property property = new Property();
		property.setPropertyId(likeRequestDTO.getIdPost());
		
		newLike.setUser(user);
		newLike.setProperty(property);
		
		return newLike;
	}
	
	public LikeRequestDTO convertToLikeDTO(Like like) {
		LikeRequestDTO newDTO = new LikeRequestDTO();
		newDTO.setIdPost(like.getProperty().getPropertyId());
		newDTO.setIdUser(like.getUser().getId());
		return newDTO;
	}
	
	public List<LikeRequestDTO> arraytoDTO (List<Like> likes) {
		List<LikeRequestDTO> newList = new ArrayList<>();
		for (Like like : likes) {
			newList.add(convertToLikeDTO(like));
		}
		return newList;
	}
}
