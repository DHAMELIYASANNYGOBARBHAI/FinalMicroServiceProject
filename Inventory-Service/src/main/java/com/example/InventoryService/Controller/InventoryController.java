package com.example.InventoryService.Controller;

import com.example.InventoryService.Service.InventoryService;
import com.example.InventoryService.dto.InventoryRequest;
import com.example.InventoryService.dto.InventoryResponse;
import com.example.InventoryService.model.Inventory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController
{
    private  final InventoryService inventoryService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryRequest addInventory(@RequestBody InventoryRequest inventoryRequest)
    {
        return inventoryService.addInventory(inventoryRequest);
    }


    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam(name="skuCode") List<String> skuCode)
    {
        return inventoryService.isInStock(skuCode);
    }


}
