package com.backend.stayEasy.sevice.impl;

import java.util.UUID;

import com.backend.stayEasy.dto.AddressDTO;

public interface IAddressService {
	AddressDTO getAddressById(UUID id);
}
