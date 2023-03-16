package com.example.InventoryService.Service;

import com.example.InventoryService.Repository.InventoryRepo;
import com.example.InventoryService.dto.InventoryRequest;
import com.example.InventoryService.dto.InventoryResponse;
import com.example.InventoryService.model.Inventory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepo inventoryRepo;

   @Transactional(readOnly = true)
   @SneakyThrows
    public List<InventoryResponse> isInStock(List<String> skuCode)
    {
        log.info("Wait Started");
//        Thread.sleep(10000);
        log.info(("Wait Ended"));
         return  inventoryRepo.findBySkuCodeIn(skuCode).stream()
                 .map(inventory ->
                     InventoryResponse.builder()
                                      .skuCode(inventory.getSkuCode())
                                      .isInStock(inventory.getQuantity() > 0)
                                      .build()
                 ).toList();
    }

    public InventoryRequest addInventory(InventoryRequest inventoryRequest)
    {
        Inventory inventory = Inventory.builder()
                .skuCode(inventoryRequest.getSkuCode())
                .quantity(inventoryRequest.getQuantity()).build();

        inventoryRepo.save(inventory);

       return inventoryRequest;
    }
}
