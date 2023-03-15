package com.example.Product.Service.service;

import com.example.Product.Service.dto.ProductRequest;
import com.example.Product.Service.dto.ProductResponse;
import com.example.Product.Service.model.Product;
import com.example.Product.Service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest)
    {
        Product product =
                Product.builder().name(productRequest.getName())
                       .price(productRequest.getPrice())
                       .description(productRequest.getDescription())
                       .build();

        productRepository.save(product);
        log.info("Product {} save in Mongodb",product.getId());

    }


    public List<ProductResponse> getAllProducts()
    {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapTOProductResponse).toList();

    }

    private ProductResponse mapTOProductResponse(Product product)
    {
        return ProductResponse.builder()
                .id(product.getId())
                .description(product.getDescription())
                .price(product.getPrice())
                .name(product.getName())
                              .build();
    }
}
