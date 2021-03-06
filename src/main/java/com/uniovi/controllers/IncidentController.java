package com.uniovi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uniovi.entities.Incident;
import com.uniovi.services.AgentsService;
import com.uniovi.services.KafkaService;
import com.uniovi.util.exception.AgentNotFoundException;

@Controller
public class IncidentController {
	
	@Autowired
	AgentsService agentsService;
	
	@Autowired
	KafkaService kafkaService;
	
	@RequestMapping(value="/incident/create", method=RequestMethod.POST)
	@ResponseBody
	public String createIncident(@RequestBody Incident incident) throws Exception {
		System.out.println(incident.toString());
		String username = incident.getUsername();
		String password = incident.getPassword();
		if (!agentsService.existsAgent(username, password)) {
			throw new AgentNotFoundException();
		}
		
		
		kafkaService.sendToKafka(incident);
		return "Incident correctly sent!";
	}

}
