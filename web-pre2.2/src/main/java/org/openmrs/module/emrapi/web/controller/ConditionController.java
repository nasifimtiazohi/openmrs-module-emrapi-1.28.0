package org.openmrs.module.emrapi.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.api.ConceptService;
import org.openmrs.api.PatientService;
import org.openmrs.module.emrapi.conditionslist.ConditionService;
import org.openmrs.module.emrapi.conditionslist.contract.Condition;
import org.openmrs.module.emrapi.conditionslist.contract.ConditionHistory;
import org.openmrs.module.emrapi.conditionslist.contract.ConditionHistoryMapper;
import org.openmrs.module.emrapi.conditionslist.contract.ConditionMapper;
import org.openmrs.module.webservices.rest.web.v1_0.controller.BaseRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/emrapi")
public class ConditionController extends BaseRestController {
	
	ConditionMapper conditionMapper = new ConditionMapper();
	
	ConditionHistoryMapper conditionHistoryMapper = new ConditionHistoryMapper(conditionMapper);
	
	ConditionService conditionService;
	
	PatientService patientService;
	
	ConceptService conceptService;
	
	@Autowired
	public ConditionController(ConditionService conditionService, PatientService patientService,
	                           ConceptService conceptService) {
		this.conditionService = conditionService;
		this.patientService = patientService;
		this.conceptService = conceptService;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/conditionhistory")
	@ResponseBody
	public List<ConditionHistory> getConditionHistory(@RequestParam("patientUuid") String patientUuid) {
		List<org.openmrs.module.emrapi.conditionslist.ConditionHistory> conditionHistory = conditionService.getConditionHistory(
				patientService.getPatientByUuid(patientUuid));
		
		return conditionHistoryMapper.map(conditionHistory);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/condition")
	@ResponseBody
	public List<Condition> getCondition(@RequestParam("conditionUuid") String conditionUuid) {
		List <Condition> conditions =new ArrayList<Condition>();
		org.openmrs.module.emrapi.conditionslist.Condition condition = conditionService.getConditionByUuid(conditionUuid);
		conditions.add(conditionMapper.map(condition));
		return conditions;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/condition")
	@ResponseBody
	public List<Condition> save(@RequestBody Condition[] conditions) {
		List<Condition> savedConditions = new ArrayList<Condition>();
		for (Condition condition : conditions) {
			org.openmrs.module.emrapi.conditionslist.Condition savedCondition = conditionService.save(conditionMapper.map(condition));
			savedConditions.add(conditionMapper.map(savedCondition));
		}
		return savedConditions;
	}
}
