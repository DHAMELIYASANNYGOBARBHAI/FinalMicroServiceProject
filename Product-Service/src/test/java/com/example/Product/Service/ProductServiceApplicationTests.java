package com.example.Product.Service;

import com.example.Product.Service.dto.ProductRequest;
import com.example.Product.Service.model.Product;
import com.example.Product.Service.repository.ProductRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceApplicationTests {

	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer();

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;


    @Autowired
	ProductRepository productRepository;



	@DynamicPropertySource
	static  void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry)
	{
        dynamicPropertyRegistry.add("spring.data.mongodb.uri",mongoDBContainer::getReplicaSetUrl);
	}
	@Test
	void contextLoads() {
	}


	@Test
	void shouldCreateProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
        String productRequestString = objectMapper.writeValueAsString(productRequest);
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
											  .contentType(MediaType.APPLICATION_JSON)
											  .content(productRequestString))
											  .andExpect(status().isCreated());

		Assertions.assertEquals(1,productRepository.findAll().size());

	}

	@Test
	void getAllProductTest() throws Exception
	{
		Product  product = Product.builder()
								  .id("1")
								  .name("nokia")
								  .description("it is good")
								  .price(BigDecimal.valueOf(1200))
								  .build();

		List<Product> products = new ArrayList<>();
		products.add(product);
		int expected = productRepository.findAll().size()+1;
		productRepository.save(product);
		String productsRequestString = objectMapper.writeValueAsString(products); // expected

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/api/product"))
			   .andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

	String productsResponseString = mvcResult.getResponse().getContentAsString(); //actual

//		Assertions.assertEquals(productsRequestString,productsResponseString);
		int actual = productRepository.findAll().size();

		Assertions.assertEquals(expected,actual);
	}

	private ProductRequest getProductRequest()
	{
		return ProductRequest.builder()
							 .name("nokia")
							 .description("it is good")
							 .price(BigDecimal.valueOf(1200))
							 .build();
	}

}
