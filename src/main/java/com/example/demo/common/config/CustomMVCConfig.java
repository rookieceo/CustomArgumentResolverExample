package com.example.demo.common.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.common.AuthorisedArgumentResolver;

@Configuration
public class CustomMVCConfig implements WebMvcConfigurer {
	@Autowired
	private AuthorisedArgumentResolver authorisedArgumentResolver;

	// 우리가 만든 ArgumentResolver를 추가한다.
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(this.authorisedArgumentResolver);
	}
}