package com.example.demo.common;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerMapping;

import com.example.demo.service.ADTO;
import com.example.demo.service.AService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthorisedArgumentResolver implements HandlerMethodArgumentResolver {

	@Autowired
	private AService service;

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.hasParameterAnnotation(Authorised.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory) throws Exception {

		Authorised authorised = parameter.getParameterAnnotation(Authorised.class);
		String annValue = authorised.value();

		// PathVariable 값을 가져온다.
		Map<?, ?> pathVariableMap = (Map<?, ?>) webRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);

		Long keyIndex = Long.valueOf(pathVariableMap.get(annValue).toString());
		// 1. Get BDTO at DB by PathVariable keyIndex
		ADTO dto = this.service.getADTOByKeyIndex(keyIndex);
		if (dto == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ADTO Not Found");
		}
		// 2. Get Login Spring Security User Object
		User loginUser = (User) ((Authentication) webRequest.getUserPrincipal()).getPrincipal();
		// 3. Compare bDTO, loginUser
		boolean isAuthorized = this.checkIfIsCurrentlyAuthorised(dto, loginUser);

		if (isAuthorized) {

			if (ADTO.class.isAssignableFrom(parameter.getParameterType())) {
				return dto;
			} else if (Long.class.isAssignableFrom(parameter.getParameterType()) ||
				long.class.isAssignableFrom(parameter.getParameterType())) {
				return keyIndex;
			} else {
				return pathVariableMap.get(annValue);
			}
		} else {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied");
		}
	}

	private boolean checkIfIsCurrentlyAuthorised(ADTO dto, User user) throws Exception {
		log.debug("dto.getKeyIndex() : {}", dto.getKeyIndex());
		log.debug("dto.getOwner() : {}", dto.getOwner());
		log.debug("loginUser.getUsername() : {}", user.getUsername());

		// DTO의 Owner값과 로그인 유저의 ID로 권한을 체크, 편의상 getUsername으로 비교
		return dto.getOwner().equals(user.getUsername());
	}

}
