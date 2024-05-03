package com.backend.stayEasy.sevice.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;

import com.backend.stayEasy.dto.DataPropertyExploreDTO;
import com.backend.stayEasy.dto.PropertyDTO;
import com.backend.stayEasy.entity.Property;

public interface IPropertyService {

	DataPropertyExploreDTO findAll();

	DataPropertyExploreDTO findAll(Pageable pageable);

	PropertyDTO findById(UUID id);

	PropertyDTO add(PropertyDTO propertyDTO);

	PropertyDTO update(UUID propertyId, PropertyDTO updatePropertyDTO);

	Property delete(UUID propertyId);

	List<PropertyDTO> findByCategory(UUID categoryId);


	List<PropertyDTO> findAllPropertiesByHostId(UUID hostId);

	List<PropertyDTO> findByPropertyNameOrAddressContainingIgnoreCase(String keySearch);
}
