package com.example.InventoryService.Service;

import com.example.InventoryService.Repository.InventoryRepo;
import com.example.InventoryService.dto.InventoryRequest;
import com.example.InventoryService.model.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepo inventoryRepo;

   @Transactional(readOnly = true)
    public boolean isInStock(String skuCode)
    {
         return inventoryRepo.findBySkuCode(skuCode).isPresent();
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
