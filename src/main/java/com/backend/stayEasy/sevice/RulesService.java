package com.backend.stayEasy.sevice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.stayEasy.convertor.RulesConverter;
import com.backend.stayEasy.dto.RulesDTO;
import com.backend.stayEasy.repository.RulesRepository;
import com.backend.stayEasy.sevice.impl.IRulesService;

@Service
public class RulesService implements IRulesService{
	
	@Autowired
	private RulesRepository rulesRepository;
	
	@Autowired
	private RulesConverter rulesConverter;

	@Override
	public List<RulesDTO> findAll() {
		// TODO Auto-generated method stub
		return rulesConverter.arrayToDTO(rulesRepository.findAll());
	}

}
