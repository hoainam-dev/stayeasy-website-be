package com.backend.stayEasy.convertor;

import com.backend.stayEasy.dto.PropertyUtilitiesDTO;
import com.backend.stayEasy.entity.PropertyUilitis;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;



@Component
public class PropertyUtilitiesConverter {
	public PropertyUtilitiesDTO toDTO(PropertyUilitis propertyUtiliti) {
		PropertyUtilitiesDTO propertyUtilitiesDTO = new PropertyUtilitiesDTO();
		propertyUtilitiesDTO.setUtilitiesId(propertyUtiliti.getUtilities().getUtilitiId());
		propertyUtilitiesDTO.setUtilitiesName(propertyUtiliti.getUtilities().getUtilitiesName());
		propertyUtilitiesDTO.setType(propertyUtiliti.getUtilities().getType());
		return propertyUtilitiesDTO;
	}
	
	public List<PropertyUtilitiesDTO> arrayToDTO(List<PropertyUilitis> propertyUtilitiesList) {
		List<PropertyUtilitiesDTO> propertyUtilitiesDTOList = new ArrayList<>();
		for (PropertyUilitis propertyUtiliti : propertyUtilitiesList) {
			propertyUtilitiesDTOList.add(toDTO(propertyUtiliti));
		}
		return propertyUtilitiesDTOList;
	}
}
