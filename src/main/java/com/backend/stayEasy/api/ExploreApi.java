package com.backend.stayEasy.api;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stayEasy.convertor.LikeConverter;
import com.backend.stayEasy.convertor.PropertyConverter;
import com.backend.stayEasy.dto.DataPropertyExploreDTO;
import com.backend.stayEasy.dto.LikeRequestDTO;
import com.backend.stayEasy.dto.PropertyDTO;
import com.backend.stayEasy.entity.Like;
import com.backend.stayEasy.entity.Property;
import com.backend.stayEasy.repository.ExploreRepository;
import com.backend.stayEasy.repository.LikeRepository;


@RestController
@CrossOrigin
@RequestMapping("/api/v1/stayeasy/explore")
public class ExploreApi {
	@Autowired
	private ExploreRepository exploreRepository;
	
	@Autowired
	private PropertyConverter propertyConverter;
	
	@Autowired
	private LikeRepository likeRepository;
	
	@Autowired
	private LikeConverter likeConverter;
	
	@GetMapping()
	public List<PropertyDTO> getAllProperties() {
		List<Property> propertyList = exploreRepository.findAll();
		List<PropertyDTO> propertyDTOs = new ArrayList<>();
		for (Property property : propertyList) {
			propertyDTOs.add(propertyConverter.toDTO(property));
		}
        return propertyDTOs;
    }
	
	 @GetMapping("/search")
	    public DataPropertyExploreDTO searchExplore(
	            @RequestParam("keySearch") String keySearch,
	            @RequestParam("page") int page,
	            @RequestParam("size") int size
	    ) {
	        System.out.println("keysearch: " + keySearch);
	        System.out.println("page: " + page);
	        System.out.println("size: " + size);
	        
	        // Tạo đối tượng Pageable từ page và size
	        Pageable pageable = PageRequest.of(page, size);
	        System.out.println("pageable : " + pageable);
	        // Lấy tổng số lượng bản ghi
	        long totalCount = exploreRepository.count();
	        
	        // Trả về kết quả truy vấn kèm theo tổng số lượng bản ghi
	        Page<Property> properties = exploreRepository.findByPropertyNameOrDescriptionContainingIgnoreCase(keySearch, pageable);
	        
			List<PropertyDTO> propertyDTOs = new ArrayList<>();
			
			for (Property property : properties) {
				 PropertyDTO propertyDTO = propertyConverter.toDTO(property);
				List<Like> likes = likeRepository.findByPropertyPropertyId(property.getPropertyId()); // get like tương ứng mỗi property
			
				List<LikeRequestDTO> likeRequestDTOs = likeConverter.arraytoDTO(likes);
				
				 propertyDTO.setLikeList(likeRequestDTOs);
				 propertyDTOs.add(propertyDTO);
				 
			
			}
			
	        return new DataPropertyExploreDTO(totalCount, propertyDTOs);
	    }
	
	
	
	@GetMapping("/search/suggest")
	public List<PropertyDTO> searchExploreSuggest(@RequestParam("keySearch") String keySearch) {
		System.out.println("keysearch suggest: " + keySearch);
		List<Property> searchResults = exploreRepository.findByPropertyNameOrDescriptionContainingIgnoreCaseOrderByRatingDesc(keySearch);
		List<PropertyDTO> propertyDTOs = new ArrayList<>();
		for (Property property : searchResults) {
			propertyDTOs.add(propertyConverter.toDTO(property));
		}
        return propertyDTOs;
	}

}