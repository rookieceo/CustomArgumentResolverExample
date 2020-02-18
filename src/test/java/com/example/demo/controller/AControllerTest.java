package com.example.demo.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@SpringBootTest
class AControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext ctx;

	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.ctx)
			.addFilters(new CharacterEncodingFilter(StandardCharsets.UTF_8.toString(), true)) // 필터 추가
			.apply(springSecurity())
			.build();
	}

	@Test
	final void testT1API() throws Exception {
		this.mockMvc.perform(get("/api/type1/1"))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser("user2")
	final void testT2API() throws Exception {
		// user2 라는 Mockuser를 통해 해당 API로 접근이 가능한지 확인한다.

		// 1. 실패 응답 확인 : 접근 불가
		this.mockMvc.perform(get("/api/type2/3"))
			.andDo(print())
			.andExpect(MockMvcResultMatchers.status().isForbidden());

		// 2. 정상 응답 확인 및 DTO 출력 : A Service 호출 결과 DTO가 맞는지 확인
		this.mockMvc.perform(get("/api/type2/2"))
			.andDo(print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(jsonPath("$.keyIndex", is(2)))
			.andExpect(jsonPath("$.owner", is("user2")));
	}

	@Test
	@WithMockUser("user3")
	final void testT3API() throws Exception {
		// user3 라는 Mockuser를 통해 해당 API로 접근이 가능한지 확인한다.

		// 1. 실패 응답 확인 : 접근 불가
		this.mockMvc.perform(get("/api/type3/2"))
			.andDo(print())
			.andExpect(MockMvcResultMatchers.status().isForbidden());

		// 2. 정상 응답 확인 및 DTO 출력 : A Service 호출 결과 DTO가 맞는지 확인
		this.mockMvc.perform(get("/api/type3/3"))
			.andDo(print())
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(jsonPath("$.keyIndex", is(3)))
			.andExpect(jsonPath("$.owner", is("user3")));
	}

}
