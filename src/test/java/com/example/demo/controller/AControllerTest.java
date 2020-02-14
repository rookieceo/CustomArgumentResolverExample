package com.example.demo.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
class AControllerTest {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext ctx;

	@BeforeEach
	void setUp() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.ctx)
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
		this.mockMvc.perform(get("/api/type2/2"))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser("user3")
	final void testT3API() throws Exception {
		this.mockMvc.perform(get("/api/type3/3"))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}

}
