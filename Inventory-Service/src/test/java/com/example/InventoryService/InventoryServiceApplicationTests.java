package com.example.InventoryService;

import com.example.InventoryService.Repository.InventoryRepo;
import com.example.InventoryService.dto.InventoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class InventoryServiceApplicationTests {

	@Container
	static MySQLContainer mySQLContainer = new MySQLContainer<>("mysql:5.7").withDatabaseName("test");

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	InventoryRepo inventoryRepo;


	@Test
	void contextLoads() {
	}

	@Test
	void addInventory() throws Exception {

		InventoryRequest inventoryRequest = getInventoryRequest();
		String inventoryRequestString  = objectMapper.writeValueAsString(inventoryRequest);
		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/inventory")
																					.contentType(MediaType.APPLICATION_JSON)
																					.content(inventoryRequestString))
													 .andExpect(status().isCreated()).andReturn();

		String inventory = mvcResult.getResponse().getContentAsString();
		Assertions.assertEquals(inventory,inventoryRequestString);

	}



	private InventoryRequest getInventoryRequest()
	{
		return  InventoryRequest.builder()
								.skuCode("Aman")
								.quantity(100)
								.build();
	}

}
