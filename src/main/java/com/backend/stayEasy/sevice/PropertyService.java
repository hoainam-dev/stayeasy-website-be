package com.backend.stayEasy.sevice;

import java.sql.Date;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.stayEasy.convertor.LikeConverter;
import com.backend.stayEasy.convertor.PropertyConverter;
import com.backend.stayEasy.dto.CategoryDTO;
import com.backend.stayEasy.dto.DataPropertyExploreDTO;
import com.backend.stayEasy.dto.ImagesDTO;
import com.backend.stayEasy.dto.LikeRequestDTO;
import com.backend.stayEasy.dto.PropertyDTO;
import com.backend.stayEasy.dto.PropertyUtilitiesDTO;
import com.backend.stayEasy.dto.RulesDTO;
import com.backend.stayEasy.entity.Category;
import com.backend.stayEasy.entity.Feedback;
import com.backend.stayEasy.entity.FeedbackTrip;
import com.backend.stayEasy.entity.Images;
import com.backend.stayEasy.entity.Like;
import com.backend.stayEasy.entity.Property;
import com.backend.stayEasy.entity.PropertyCategory;
import com.backend.stayEasy.entity.PropertyRules;
import com.backend.stayEasy.entity.PropertyUilitis;
import com.backend.stayEasy.entity.Rules;
import com.backend.stayEasy.entity.Utilities;

import com.backend.stayEasy.repository.CategoryRepository;
import com.backend.stayEasy.repository.FeedbackRepository;

import com.backend.stayEasy.repository.FeedbackTripRepository;

import com.backend.stayEasy.repository.IImageRepository;
import com.backend.stayEasy.repository.IPropertyCategoryRepository;
import com.backend.stayEasy.repository.IPropertyRepository;
import com.backend.stayEasy.repository.LikeRepository;
import com.backend.stayEasy.repository.PropertyRulesRepository;
import com.backend.stayEasy.repository.PropertyUilitisRepository;
import com.backend.stayEasy.repository.RulesRepository;
import com.backend.stayEasy.repository.UtilitiesRepository;
import com.backend.stayEasy.sevice.impl.IPropertyService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PropertyService implements IPropertyService {

	@Autowired
	private IPropertyRepository propertyRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private PropertyConverter propertyConverter;

	@Autowired
	private IImageRepository imageRepository;

	@Autowired
	private IPropertyCategoryRepository propertyCategoryRepository;

	@Autowired
	private LikeRepository likeRepository;

	@Autowired
	private LikeConverter likeConverter;

	@Autowired
	private RulesRepository rulesRepository;

	@Autowired
	private UtilitiesRepository utilitiesRepository;

	@Autowired
	private PropertyRulesRepository propertyRulesRepository;

	@Autowired
	private PropertyUilitisRepository propertyUilitisRepository;

	@Autowired
	private FeedbackTripRepository feedbackRepository;

	@Override
	public DataPropertyExploreDTO findAll(Pageable pageable) {
		Page<Property> tempList = propertyRepository.findAll(pageable);
		long totalCount = propertyRepository.count();
		List<PropertyDTO> result = new ArrayList<>();

		for (Property p : tempList) {
			List<Like> likes = likeRepository.findByPropertyPropertyId(p.getPropertyId());
			List<LikeRequestDTO> likeRequestDTOs = likeConverter.arraytoDTO(likes);
			PropertyDTO temp = propertyConverter.toDTO(p);
			float rating = getRatingProperty(p.getPropertyId());
			temp.setLikeList(likeRequestDTOs);
			temp.setRating(rating);
			result.add(temp);
		}
//		return result;
		DataPropertyExploreDTO newData = new DataPropertyExploreDTO();
		newData.setProperties(result);
		newData.setTotalCount(totalCount);
		return newData;
	}

	@Override
	public PropertyDTO findById(UUID id) {
		Property property = propertyRepository.findByPropertyId(id);
		float rating = getRatingProperty(id);
		property.setRating(rating);
		return propertyConverter.toDTO(property);
	}

	@Override
	public PropertyDTO add(PropertyDTO propertyDTO) {
		Property property = new Property();
		List<Images> images = new ArrayList<>();
		List<PropertyCategory> propertyCategory = new ArrayList<>();
		List<PropertyRules> propertyRules = new ArrayList<>();
		List<PropertyUilitis> propertyUtilities = new ArrayList<>();
		property = propertyConverter.toEntity(propertyDTO);

		for (ImagesDTO i : propertyDTO.getImagesList()) {
			images.add(new Images(i.getUrl(), i.getDescription(), property));
		}

		for (CategoryDTO categoryDTO : propertyDTO.getCategories()) {
			PropertyCategory temp = new PropertyCategory();
			temp.setProperty(property);
			Optional<Category> categoryOp = categoryRepository.findById(categoryDTO.getCategoryId());
			if (categoryOp.isPresent()) {
				Category category = categoryOp.get();
				temp.setCategory(category);
			}
			propertyCategory.add(temp);
		}

		for (RulesDTO rulesDTO : propertyDTO.getRulesList()) {
			PropertyRules temp = new PropertyRules();
			temp.setProperty(property);
			Optional<Rules> rulesOp = rulesRepository.findById(rulesDTO.getRulesId());
			if (rulesOp.isPresent()) { // Kiểm tra xem giá trị tồn tại trong Optional hay không
				Rules rules = rulesOp.get(); // Trích xuất giá trị User từ Optional
				temp.setRules(rules);
			}
			propertyRules.add(temp);
		}

		for (PropertyUtilitiesDTO propertyUtilitiesDTO : propertyDTO.getPropertyUtilitis()) {
			PropertyUilitis temp = new PropertyUilitis();
			temp.setProperty(property);
			Optional<Utilities> utilitiesOp = utilitiesRepository.findById(propertyUtilitiesDTO.getUtilitiesId());
			if (utilitiesOp.isPresent()) {
				Utilities utilities = utilitiesOp.get();
				temp.setUtilities(utilities);
			}
			propertyUtilities.add(temp);
		}

		LocalDate today = LocalDate.now();
		property.setCreateAt(Date.valueOf(today));
		property.setImages(images);
		property.setPropertyCategories(propertyCategory);
		property.setPropertyRules(propertyRules);
		property.setPropertyUilitis(propertyUtilities);

		propertyRepository.save(property);

		return propertyConverter.toDTO(property);
	}

	@Override
	public PropertyDTO update(UUID propertyId, PropertyDTO updatePropertyDTO) {
		Optional<Property> propertyOptional = propertyRepository.findById(propertyId);

		if (propertyOptional.isPresent()) {
			Property existingProperty = propertyOptional.get();
			Property updatedProperty = propertyConverter.toEntity(updatePropertyDTO);

			// Copy các thông tin từ updatedProperty vào existingProperty
			existingProperty.setPropertyName(updatedProperty.getPropertyName());
			existingProperty.setDescription(updatedProperty.getDescription());
			existingProperty.setThumbnail(updatedProperty.getThumbnail());
			existingProperty.setPrice(updatedProperty.getPrice());
			existingProperty.setNumGuests(updatedProperty.getNumGuests());
			existingProperty.setDiscount(updatedProperty.getDiscount());
			existingProperty.setNumBathRoom(updatedProperty.getNumBathRoom());
			existingProperty.setNumBedRoom(updatedProperty.getNumBedRoom());
			existingProperty.setServiceFee(updatedProperty.getServiceFee());

			checkUpdateProperty(existingProperty, updatePropertyDTO);

			Property result = propertyRepository.save(existingProperty);

			return propertyConverter.toDTO(result);
		} else {
			throw new EntityNotFoundException("Property not found!");
		}

	}

	@Override
	public Property delete(UUID propertyId) {

		if (propertyRepository.existsById(propertyId)) {
			propertyRepository.deleteById(propertyId);
		} else {
			throw new EntityNotFoundException("Property not found with ID: " + propertyId);
		}

		return null;
	}

	@Override
	public List<PropertyDTO> findByCategory(UUID categoryId) {
		List<PropertyDTO> result = new ArrayList<>();
		List<PropertyCategory> propertyCategory = propertyCategoryRepository.findByCategoryCategoryId(categoryId);

		for (PropertyCategory p : propertyCategory) {
			Property temp = propertyRepository.findByPropertyId(p.getProperty().getPropertyId());
			List<Like> likes = likeRepository.findByPropertyPropertyId(temp.getPropertyId());
			List<LikeRequestDTO> likeRequestDTOs = likeConverter.arraytoDTO(likes);
			PropertyDTO temp2 = propertyConverter.toDTO(temp);
			temp2.setLikeList(likeRequestDTOs);
			result.add(temp2);
		}
		return result;
	}

	public List<PropertyDTO> findAllPropertiesByHostId(UUID id) {
		// Truy vấn cơ sở dữ liệu để lấy danh sách các property có userId giống với
		// hostId
		List<Property> properties = propertyRepository.findAllByUserId(id);

		// Chuyển đổi danh sách các property sang danh sách PropertyDTO
		return properties.stream().map(propertyConverter::toDTO).collect(Collectors.toList());
	}

	@Override
	public List<PropertyDTO> findByPropertyNameOrAddressContainingIgnoreCase(String keySearch) {
		List<Property> propertyList = propertyRepository.findByPropertyNameOrAddressContainingIgnoreCase(keySearch);
		return propertyConverter.arrayToDTO(propertyList);
	}

	// Check update
	public void checkUpdateProperty(Property existingProperty, PropertyDTO updatePropertyDTO) {
		List<PropertyCategory> categoryToRemove = new ArrayList<>();
		List<PropertyRules> rulesToMove = new ArrayList<>();
		List<PropertyUilitis> utilitiesToMove = new ArrayList<>();
		List<Images> imagesToRemove = new ArrayList<>();

		for (PropertyCategory existingCategory : existingProperty.getPropertyCategories()) {
			boolean existsInUpdate = false;
			for (CategoryDTO categoryDTO : updatePropertyDTO.getCategories()) {
				if (existingCategory.getCategory().getCategoryId().equals(categoryDTO.getCategoryId())) {
					existsInUpdate = true;
					break;
				}
			}
			if (!existsInUpdate) {
				categoryToRemove.add(existingCategory);
			}
		}
		existingProperty.getPropertyCategories().removeAll(categoryToRemove);
		for (PropertyCategory propertyCategory : categoryToRemove) {
			propertyCategoryRepository.delete(propertyCategory);
		}
		// Cập nhật danh sách category của property từ danh sách cập nhật
		for (CategoryDTO categoryDTO : updatePropertyDTO.getCategories()) {
			boolean exists = false;
			for (PropertyCategory existingCategory : existingProperty.getPropertyCategories()) {
				if (existingCategory.getCategory().getCategoryId().equals(categoryDTO.getCategoryId())) {
					exists = true;
					break;
				}
			}
			// Nếu category không tồn tại trong danh sách property, thêm nó vào
			if (!exists) {
				PropertyCategory newCategory = new PropertyCategory();
				newCategory.setProperty(existingProperty);
				Optional<Category> temp = categoryRepository.findById(categoryDTO.getCategoryId());
				newCategory.setCategory(temp.get());
				existingProperty.getPropertyCategories().add(newCategory);
			}
		}

		// xoá image không update
		for (Images existingImage : existingProperty.getImages()) {
			boolean existsInUpdate = false;
			for (ImagesDTO imagesDTO : updatePropertyDTO.getImagesList()) {
				if (existingImage.getUrl().equals(imagesDTO.getUrl())) {
					existsInUpdate = true;
					break;
				}
			}
			if (!existsInUpdate) {
				// Xóa ảnh từ cơ sở dữ liệu
				imageRepository.delete(existingImage);
				imagesToRemove.add(existingImage);
			}
		}
		existingProperty.getImages().removeAll(imagesToRemove);

		// Check đã có images chưa
		for (ImagesDTO imagesDTO : updatePropertyDTO.getImagesList()) {
			boolean exists = false;
			for (Images existingImage : existingProperty.getImages()) {
				if (existingImage.getUrl().equals(imagesDTO.getUrl())) {
					exists = true;
					break;
				}
			}
			// chưa có add vô
			if (!exists) {
				Images newImage = new Images(imagesDTO.getUrl(), imagesDTO.getDescription(), existingProperty);
				existingProperty.getImages().add(newImage);
			}
		}

		for (PropertyRules existingRules : existingProperty.getPropertyRules()) {
			boolean existsInUpdate = false;
			for (RulesDTO rulesDTO : updatePropertyDTO.getRulesList()) {
				if (existingRules.getRules().getRulesId().equals(rulesDTO.getRulesId())) {
					existsInUpdate = true;
					break;
				}
			}
			if (!existsInUpdate) {
				rulesToMove.add(existingRules);
			}
		}
		existingProperty.getPropertyRules().removeAll(rulesToMove);
		for (PropertyRules propertyRules : rulesToMove) {
			propertyRulesRepository.delete(propertyRules);
		}
		// Cập nhật danh sách rules của property từ danh sách cập nhật
		for (RulesDTO rulesDTO : updatePropertyDTO.getRulesList()) {
			boolean exists = false;
			for (PropertyRules existingRules : existingProperty.getPropertyRules()) {
				if (existingRules.getRules().getRulesId().equals(rulesDTO.getRulesId())) {
					exists = true;
					break;
				}
			}
			// Nếu rules không tồn tại trong danh sách property, thêm nó vào
			if (!exists) {
				PropertyRules newRules = new PropertyRules();
				newRules.setProperty(existingProperty);
				Optional<Rules> temp = rulesRepository.findById(rulesDTO.getRulesId());
				newRules.setRules(temp.get());
				existingProperty.getPropertyRules().add(newRules);
			}
		}

		// lưu utilities cần update

		for (PropertyUilitis existingUtilities : existingProperty.getPropertyUilitis()) {
			boolean existsInUpdate = false;
			for (PropertyUtilitiesDTO utilitiesDTO : updatePropertyDTO.getPropertyUtilitis()) {
				if (existingUtilities.getUtilities().getUtilitiId().equals(utilitiesDTO.getUtilitiesId())) {
					existsInUpdate = true;
					break;
				}
			}
			if (!existsInUpdate) {
				utilitiesToMove.add(existingUtilities);
			}
		}
		existingProperty.getPropertyUilitis().removeAll(utilitiesToMove);
		for (PropertyUilitis propertyUtilities : utilitiesToMove) {
			propertyUilitisRepository.delete(propertyUtilities);
		}
		// Cập nhật danh sách utilies và số lượng nếu thay đổi của property từ danh sách
		// cập nhật
		for (PropertyUtilitiesDTO utilitiesDTO : updatePropertyDTO.getPropertyUtilitis()) {
			boolean exists = false;
			for (PropertyUilitis existingUtilities : existingProperty.getPropertyUilitis()) {
				if (existingUtilities.getUtilities().getUtilitiId().equals(utilitiesDTO.getUtilitiesId())) {
					exists = true;
					break;
				}
			}
			// Nếu utilities không tồn tại trong danh sách property, thêm nó vào
			if (!exists) {
				PropertyUilitis newUtilities = new PropertyUilitis();
				newUtilities.setProperty(existingProperty);
				Optional<Utilities> temp = utilitiesRepository.findById(utilitiesDTO.getUtilitiesId());
				newUtilities.setUtilities(temp.get());
				existingProperty.getPropertyUilitis().add(newUtilities);
			}
		}

	}

	public float getRatingProperty(UUID propertyId) {
	    List<FeedbackTrip> feedbackList = feedbackRepository.findByPropertyPropertyId(propertyId);
	    float total = 0;
	    for (FeedbackTrip feedback : feedbackList) {
	        total += feedback.getRating();
	    }
	    if (feedbackList.size() > 0) {
	        return total / feedbackList.size(); 
	    } else {
	        return 0;
	    }
	}

	@Override
	public DataPropertyExploreDTO findAll() {
		long totalCount = propertyRepository.count();
		List<PropertyDTO> result = new ArrayList<>();

		for (Property p : propertyRepository.findAll()) {
			List<Like> likes = likeRepository.findByPropertyPropertyId(p.getPropertyId());
			List<LikeRequestDTO> likeRequestDTOs = likeConverter.arraytoDTO(likes);
			PropertyDTO temp = propertyConverter.toDTO(p);
			float rating = getRatingProperty(p.getPropertyId());
			temp.setLikeList(likeRequestDTOs);
			temp.setRating(rating);
			result.add(temp);
		}
		DataPropertyExploreDTO newData = new DataPropertyExploreDTO();
		newData.setProperties(result);
		newData.setTotalCount(totalCount);
		return newData;
		// TODO Auto-generated method stub
	}
}