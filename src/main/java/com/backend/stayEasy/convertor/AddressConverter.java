package com.backend.stayEasy.convertor;

import org.springframework.stereotype.Component;

import com.backend.stayEasy.dto.AddressDTO;
import com.backend.stayEasy.entity.Address;

@Component
public class AddressConverter {
	
	public AddressDTO toDTO(Address address) {
		AddressDTO result = new AddressDTO();
		result.setId(address.getId());
		result.setStreet(address.getStreet());
		result.setWard(address.getWard());
		result.setDistrict(address.getDistrict());
		result.setCity(address.getCity());
		result.setCountry(address.getCountry());
		return result;
	}
	
	public Address toEntity(AddressDTO addressDTO) {
		Address result = new Address();
		result.setStreet(addressDTO.getStreet());
		result.setWard(addressDTO.getWard());
		result.setDistrict(addressDTO.getDistrict());
		result.setCity(addressDTO.getCity());
		result.setCountry(addressDTO.getCountry());
		return result;
	}
	
	public Address toEntity(Address address, AddressDTO addressDTO) {
		address.setStreet(addressDTO.getStreet());
		address.setWard(addressDTO.getWard());
		address.setDistrict(addressDTO.getDistrict());
		address.setCity(addressDTO.getCity());
		address.setCountry(addressDTO.getCountry());
		return address;
	}
}
