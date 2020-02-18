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

	// Step1 - 원래 API : PathVariable Uri를 활용하여 A DTO 출력
	@GetMapping(value = "/type1/{keyIndex}")
	public ADTO type1API(@PathVariable("keyIndex") Long keyIndex) {
		return this.service.getADTOByKeyIndex(keyIndex);
	}

	// Step2 - 수정 1 권한 체크(ArgumentResolver) 추가 + AService 2번 호출
	@GetMapping(value = "/type2/{keyIndex}")
	public ADTO type2API(@Authorised("keyIndex") Long keyIndex) {
		// AuthorisedArgumentResolver에서 호출된 AService.getADTOByKeyIndex이 다시 호출됨
		return this.service.getADTOByKeyIndex(keyIndex);
	}

	// Step3 - 수정 2 권한 체크(ArgumentResolver) 추가 + AService 1번 호출
	@GetMapping(value = "/type3/{keyIndex}")
	public ADTO type3API(@Authorised("keyIndex") ADTO dto) {
		// AuthorisedArgumentResolver에서 호출된 AService.getADTOByKeyIndex 를 인자로 받아 이를 Json Result로 출력
		return dto;
	}

}
