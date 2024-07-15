package com.ivs.inventory_service.service;

import java.util.List;

import com.ivs.inventory_service.domain.InventoryItem;

public interface InventoryService {
    List<InventoryItem> getAllInventoryItems();
    InventoryItem getInventoryItemById(Long id);
    InventoryItem addInventoryItem(InventoryItem item);
    void updateInventoryItem(InventoryItem item);
    void deleteInventoryItem(Long id);
}
