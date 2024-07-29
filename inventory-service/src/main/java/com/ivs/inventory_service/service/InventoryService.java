package com.ivs.inventory_service.service;

import com.ivs.inventory_service.domain.Inventory;

import java.util.List;

public interface InventoryService {
    List<Inventory> getAllInventories();
    Inventory getInventoryById(Long id);
    Inventory getInventoryByProductId(String id);
    Inventory createInventory(Inventory inventory);
    Inventory updateInventory(Long id, Inventory inventory);
    void deleteInventory(Long id);
}
