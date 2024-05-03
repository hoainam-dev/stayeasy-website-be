package com.backend.stayEasy.sevice;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.stayEasy.convertor.AddressConverter;
import com.backend.stayEasy.dto.AddressDTO;
import com.backend.stayEasy.repository.AddressRepository;
import com.backend.stayEasy.sevice.impl.IAddressService;

@Service
public class AddressService implements IAddressService{
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private AddressConverter addressConverter;
	
	public AddressDTO getAddressById(UUID id) {
		return addressConverter.toDTO(addressRepository.findById(id).get());
	}
	
}
