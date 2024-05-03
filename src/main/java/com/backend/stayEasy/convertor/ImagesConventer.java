package com.backend.stayEasy.convertor;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.backend.stayEasy.dto.ImagesDTO;
import com.backend.stayEasy.entity.Images;

@Component
public class ImagesConventer {
	public ImagesDTO toDTO(Images images) {
		ImagesDTO imagesDTO = new ImagesDTO();
		imagesDTO.setDescription(images.getDescription());
		imagesDTO.setImageId(images.getImageId());
		imagesDTO.setUrl(images.getUrl());
		if (!images.getProperty().isNull()) {
			imagesDTO.setPropertyId(images.getProperty().getPropertyId());
		}
		return imagesDTO;
	}

	public Set<ImagesDTO> arrayToDTO(Set<Images> imagesList) {
		Set<ImagesDTO> imagesDTOList = new HashSet<>();
		for (Images image : imagesList) {
			imagesDTOList.add(toDTO(image));
		}
		Set<ImagesDTO> imagesSet = new HashSet<>(imagesDTOList);
		return imagesSet;
	}

}
