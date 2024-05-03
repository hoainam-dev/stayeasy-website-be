package com.backend.stayEasy.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stayEasy.dto.RulesDTO;
import com.backend.stayEasy.sevice.impl.IRulesService;

@RestController
@CrossOrigin
@RequestMapping(value="/api/v1/stayeasy/rules", produces = "application/json")
public class RulesAPI {
	
	@Autowired
	private IRulesService rulesService;
	
	@GetMapping("")
	public List<RulesDTO> findAll(){
		return rulesService.findAll();
	}

}
