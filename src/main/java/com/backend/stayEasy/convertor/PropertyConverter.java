package com.backend.stayEasy.convertor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.backend.stayEasy.dto.CategoryDTO;
import com.backend.stayEasy.dto.ImagesDTO;
import com.backend.stayEasy.dto.LikeRequestDTO;
import com.backend.stayEasy.dto.PropertyDTO;
import com.backend.stayEasy.dto.PropertyUtilitiesDTO;
import com.backend.stayEasy.dto.RulesDTO;
import com.backend.stayEasy.entity.Images;
import com.backend.stayEasy.entity.Property;
import com.backend.stayEasy.entity.PropertyCategory;
import com.backend.stayEasy.entity.PropertyRules;
import com.backend.stayEasy.entity.PropertyUilitis;
import com.backend.stayEasy.entity.User;
import com.backend.stayEasy.repository.UserRepository;


@Component
public class PropertyConverter {

	@Autowired
	private ImagesConventer imagesConventer;

	@Autowired
	private CategoryConverter categoryConverter;

	@Autowired
	private UserConverter userConverter;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RulesConverter rulesConverter;
	
	@Autowired
	private PropertyUtilitiesConverter propertyUtilitiesConverter;
	

	public PropertyDTO toDTO(Property property) {
		List<ImagesDTO> listImages = new ArrayList<>();
		List<CategoryDTO> listCategory = new ArrayList<>();
		List<RulesDTO> listRules = new ArrayList<>();
		List<PropertyUtilitiesDTO> listUtilities = new ArrayList<>();
		PropertyDTO propertyDTO = new PropertyDTO();
		propertyDTO.setAddress(property.getAddress());
		propertyDTO.setDescription(property.getDescription());
		propertyDTO.setDiscount(property.getDiscount());
//		propertyDTO.setNull(property.isNull());
		propertyDTO.setNumGuests(property.getNumGuests());
		propertyDTO.setPrice(property.getPrice());
		propertyDTO.setPropertyId(property.getPropertyId());
		propertyDTO.setPropertyName(property.getPropertyName());
		propertyDTO.setRating(property.getRating());
		propertyDTO.setNumBathRoom(property.getNumBathRoom());
		propertyDTO.setNumBedRoom(property.getNumBedRoom());
		propertyDTO.setServiceFee(property.getServiceFee());
		
		// CATEGORIES
		if (!property.getPropertyCategories().isEmpty()) {
			for (PropertyCategory c : property.getPropertyCategories()) {
				listCategory.add(categoryConverter.toDTO(c.getCategory()));
			}
		}
		propertyDTO.setCategories(listCategory);
		
		// THUMBNAIL
		propertyDTO.setThumbnail(property.getThumbnail());
		
		if (!property.getImages().isEmpty()) {
			for (Images i : property.getImages()) {
				listImages.add(imagesConventer.toDTO(i));
			}
		}
		propertyDTO.setImagesList(listImages);

		// RULES
		if (!property.getPropertyRules().isEmpty()) {
			for (PropertyRules c : property.getPropertyRules()) {
				listRules.add(rulesConverter.toDTO(c.getRules()));
			}
		}
		propertyDTO.setRulesList(listRules);
		
		// UTILITIESS
		if (!property.getPropertyUilitis().isEmpty()) {
			for (PropertyUilitis c : property.getPropertyUilitis()) {
				listUtilities.add(propertyUtilitiesConverter.toDTO(c));
			}
		}
		System.out.println("o day: "+listUtilities);
		propertyDTO.setPropertyUtilitis(listUtilities);
		
		// USER
		if (property.getUser() != null) {
			propertyDTO.setOwner(userConverter.toDTO(property.getUser()));
		}
		
		return propertyDTO;
	}

	// TO ENTITY
	public Property toEntity(PropertyDTO propertyDTO) {
		Property property = new Property();
		if(propertyDTO.getPropertyId()!=null) {
			property.setPropertyId(propertyDTO.getPropertyId());
		}
		property.setAddress(propertyDTO.getAddress());
		property.setDescription(propertyDTO.getDescription());
		property.setDiscount(propertyDTO.getDiscount());
		property.setNull(false);
		property.setNumGuests(propertyDTO.getNumGuests());
		property.setNumBathRoom(propertyDTO.getNumBathRoom());
		property.setNumBedRoom(propertyDTO.getNumBedRoom());
		property.setPrice(propertyDTO.getPrice());
		property.setPropertyId(propertyDTO.getPropertyId());
		property.setPropertyName(propertyDTO.getPropertyName());
		property.setServiceFee(propertyDTO.getServiceFee());
		property.setRating(propertyDTO.getRating());
		property.setThumbnail(propertyDTO.getThumbnail());
		property.setNumBathRoom(propertyDTO.getNumBathRoom());
		property.setNumBedRoom(propertyDTO.getNumBedRoom());

		Optional<User> optionalUser = userRepository.findById(propertyDTO.getOwner().getId());
				if (optionalUser.isPresent()) {
				    User user = optionalUser.get(); 
				    property.setUser(user);
				} else {
				   property.setUser(null);
				}
		return property;

	}

//	no

	public PropertyDTO toDTO(Property property, List<LikeRequestDTO> likeRequestDTOlist) {
		PropertyDTO propertyDTO = new PropertyDTO();
		propertyDTO.setAddress(property.getAddress());
		propertyDTO.setDescription(property.getDescription());
		propertyDTO.setDiscount(property.getDiscount());
		propertyDTO.setNull(property.isNull());
		propertyDTO.setNumGuests(property.getNumGuests());
		propertyDTO.setPrice(property.getPrice());
		propertyDTO.setPropertyId(property.getPropertyId());
		propertyDTO.setPropertyName(property.getPropertyName());
		propertyDTO.setRating(property.getRating());
		propertyDTO.setThumbnail(property.getThumbnail());
		propertyDTO.setOwner(userConverter.toDTO(property.getUser()));
		propertyDTO.setLikeList(likeRequestDTOlist);
		return propertyDTO;
	}
	

	public List<PropertyDTO> arrayToDTO(List<Property> propertyList) {
		List<PropertyDTO> propertyDTOList = new ArrayList<>();
		for (Property property : propertyList) {
			propertyDTOList.add(toDTO(property));
		}
		return propertyDTOList;
	}

}