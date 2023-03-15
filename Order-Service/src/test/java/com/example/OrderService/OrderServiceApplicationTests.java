package com.example.OrderService;

import com.example.OrderService.dto.OrderLineItemsDto;
import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.repository.OrderRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockReset;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
class OrderServiceApplicationTests {

	@Container
	static MySQLContainer mySQLContainer = new MySQLContainer<>();

//			.withDatabaseName("test").withPassword(
//			"test").withUsername("test");

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	OrderRepository orderRepository;


	@DynamicPropertySource
	static  void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry)
	{
		dynamicPropertyRegistry.add("spring.data.mysql.uri",mySQLContainer::getJdbcUrl);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void shouldPlaceOrder() throws Exception {
		OrderRequest orderRequest = getOrderRequest();
		String orderRequestString = objectMapper.writeValueAsString(orderRequest);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/order")
														  .contentType(MediaType.APPLICATION_JSON)
														  .content(orderRequestString))
						   .andExpect(status().isCreated()).andReturn();


		System.out.println(mvcResult.getResponse().getContentAsString());


	}

	private OrderRequest getOrderRequest()
	{
		return OrderRequest.builder()
				.orderLineItemsDtoList(getOrderLineItemsDtoList()).build();
	}

	private List<OrderLineItemsDto> getOrderLineItemsDtoList()
	{
		List<OrderLineItemsDto> orderLineItemsDtoList = new ArrayList<>();
		orderLineItemsDtoList.add(getOrderLineItemsDto());
		return orderLineItemsDtoList;
	}

	private OrderLineItemsDto getOrderLineItemsDto()
	{
		return OrderLineItemsDto.builder().
										id(2L).
										price(BigDecimal.valueOf(100))
								.skuCode("5000")
								.quantity(100)
								.build();
	}

}
