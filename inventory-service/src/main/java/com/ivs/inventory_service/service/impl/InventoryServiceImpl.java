package com.ivs.inventory_service.service.impl;

import org.springframework.stereotype.Service;

import com.ivs.inventory_service.domain.InventoryItem;
import com.ivs.inventory_service.service.InventoryService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private List<InventoryItem> inventoryItems = new ArrayList<>();
    private Long inventoryItemIdCounter = 1L;

    @Override
    public List<InventoryItem> getAllInventoryItems() {
        return inventoryItems;
    }

    @Override
    public InventoryItem getInventoryItemById(Long id) {
        return inventoryItems.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public InventoryItem addInventoryItem(InventoryItem item) {
        item.setId(inventoryItemIdCounter++);
        inventoryItems.add(item);
        return item;
    }

    @Override
    public void updateInventoryItem(InventoryItem item) {
        Optional<InventoryItem> existingItem = inventoryItems.stream()
                .filter(i -> i.getId().equals(item.getId()))
                .findFirst();
        existingItem.ifPresent(i -> {
            i.setProductId(item.getProductId());
            i.setAvailableQuantity(item.getAvailableQuantity());
        });
    }

    @Override
    public void deleteInventoryItem(Long id) {
        inventoryItems.removeIf(item -> item.getId().equals(id));
    }
}
