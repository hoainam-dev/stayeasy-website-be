package com.backend.stayEasy.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.stayEasy.dto.UtilitiesDTO;
import com.backend.stayEasy.sevice.impl.IUtilitiesService;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/stayeasy/util", produces = "application/json")
public class UtilApi {

	@Autowired
	private IUtilitiesService utilitiesService;

	@GetMapping("")
	public List<UtilitiesDTO> getAll() {

		return utilitiesService.findAll();
	}

}
