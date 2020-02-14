package com.example.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.AEntity;
import com.example.demo.repository.ARepository;

@Service
@Transactional(readOnly = true)
public class AService {

	@Autowired
	private ARepository repository;

	public ADTO getADTOByKeyIndex(Long keyIndex) {

		Optional<AEntity> entityOptional = this.repository.findById(keyIndex);

		if (entityOptional.isPresent()) {
			AEntity entity = entityOptional.get();
			return new ADTO(entity.getKeyIndex(), entity.getOwner());
		} else {
			return null;
		}

	}
}
