package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.entity.AEntity;
import com.example.demo.repository.ARepository;

@SpringBootApplication
public class CustomArgumentResolverExampleApplication implements CommandLineRunner {

	@Autowired
	private ARepository repository;

	public static void main(String[] args) {
		SpringApplication.run(CustomArgumentResolverExampleApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// keyIndex, owner 저장(1, 'user'+1)
		for (int i = 1; i < 30; i++) {
			this.repository.save(new AEntity((long) i, "user" + i));
		}
		this.repository.flush();
	}

}
