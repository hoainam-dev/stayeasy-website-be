package com.backend.stayEasy.convertor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.backend.stayEasy.dto.RulesDTO;
import com.backend.stayEasy.entity.Rules;

@Component
public class RulesConverter {
	public RulesDTO toDTO(Rules rules) {
		RulesDTO rulesDTO = new RulesDTO();
		rulesDTO.setRulesName(rules.getRulesName());
		rulesDTO.setRulesId(rules.getRulesId());
		rulesDTO.setRulesType(rules.getType());
		return rulesDTO;
	}
	
	public List<RulesDTO> arrayToDTO(List<Rules> rulesList){
		List<RulesDTO> rulesDTOList = new ArrayList<>();
		for (Rules rules : rulesList) {
			rulesDTOList.add(toDTO(rules));
		}
		return rulesDTOList;
	}
}
