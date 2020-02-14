package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.common.Authorised;
import com.example.demo.service.ADTO;
import com.example.demo.service.AService;

@RestController
@RequestMapping("/api")
public class AController {

	@Autowired
	private AService service;

	// Step1 - 일반 PathVariable 활용 로직
	@GetMapping(value = "/type1/{keyIndex}")
	public ADTO type1API(@PathVariable("keyIndex") Long keyIndex) {
		return this.service.getADTOByKeyIndex(keyIndex);
	}

	// Step2 - AuthorisedArgumentResolver를 활용하여 비교 로직 -1
	@GetMapping(value = "/type2/{keyIndex}")
	public ADTO type2API(@Authorised("keyIndex") Long keyIndex) {
		// AuthorisedArgumentResolver에서 호출된 AService.getADTOByKeyIndex이 다시 호출
		return this.service.getADTOByKeyIndex(keyIndex);
	}

	// Step3 - AuthorisedArgumentResolver를 활용하여 비교 로직 -2
	@GetMapping(value = "/type3/{keyIndex}")
	public ADTO type3API(@Authorised("keyIndex") ADTO dto) {
		// AuthorisedArgumentResolver에서 호출된 AService.getADTOByKeyIndex 1번만 호출
		return dto;
	}

}
