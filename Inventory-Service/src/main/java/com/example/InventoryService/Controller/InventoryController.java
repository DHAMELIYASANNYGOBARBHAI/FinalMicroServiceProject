package com.example.InventoryService.Controller;

import com.example.InventoryService.Service.InventoryService;
import com.example.InventoryService.dto.InventoryRequest;
import com.example.InventoryService.model.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController
{
    private  final InventoryService inventoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryRequest addInventory(@RequestBody InventoryRequest inventoryRequest)
    {
        return inventoryService.addInventory(inventoryRequest);
    }
    @GetMapping("/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable("sku-code") String skuCode)
    {
        return inventoryService.isInStock(skuCode);
    }


}
