package com.example.OrderService.service;

import com.example.OrderService.dto.InventoryResponse;
import com.example.OrderService.dto.OrderLineItemsDto;
import com.example.OrderService.dto.OrderRequest;
import com.example.OrderService.model.Order;
import com.example.OrderService.model.OrderLineItems;
import com.example.OrderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private  final OrderRepository orderRepository;
    private final WebClient webClient;


    public void placeOrder(OrderRequest orderRequest) throws IllegalAccessException {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

      List<OrderLineItems> orderLineItems =  orderRequest.getOrderLineItemsDtoList()
                                                         .stream().map(this::mapToDto).toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes =
                order.getOrderLineItemsList()
                     .stream()
                     .map(OrderLineItems::getSkuCode).toList();

      // call Inventory is product is in stock




    InventoryResponse[] inventoryResponsesArray = webClient.get()
              .uri("http://localhost:8082/api/inventory",
                      uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
              .retrieve()
              .bodyToMono(InventoryResponse[].class)
              .block();



       boolean allProductInStock =
               Arrays.stream(inventoryResponsesArray).allMatch(InventoryResponse::isInStock);



    if(allProductInStock) {

        orderRepository.save(order);
        log.info(" {} is placed",order.getId());
    }
    else {
        throw new IllegalAccessException("Product is not in stock");
    }



    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto)
    {
        return OrderLineItems.builder()
                .price(orderLineItemsDto.getPrice())
                .skuCode(orderLineItemsDto.getSkuCode())
                .quantity(orderLineItemsDto.getQuantity())
                             .build();
    }
}
