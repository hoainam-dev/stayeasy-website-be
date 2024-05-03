package com.backend.stayEasy.sevice.impl;

import java.util.List;

import com.backend.stayEasy.dto.RulesDTO;

public interface IRulesService {
	List<RulesDTO> findAll();
}
