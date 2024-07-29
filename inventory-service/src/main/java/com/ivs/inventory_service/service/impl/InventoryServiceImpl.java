package com.ivs.inventory_service.service.impl;

import com.ivs.inventory_service.domain.Inventory;
import com.ivs.inventory_service.repository.InventoryRepository;
import com.ivs.inventory_service.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Override
    public List<Inventory> getAllInventories() {
        return inventoryRepository.findAll();
    }

    @Override
    public Inventory getInventoryById(Long id) {
        Optional<Inventory> inventory = inventoryRepository.findById(id);
        return inventory.orElse(null);
    }

    @Override
    public Inventory getInventoryByProductId(String id) {
        List<Inventory> inventories = inventoryRepository.findByProductId(id);
        if (inventories != null && inventories.size() > 0) {
            return inventories.get(0);
        }
        return null;
    }

    @Override
    public Inventory createInventory(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Override
    public Inventory updateInventory(Long id, Inventory inventory) {
        if (inventoryRepository.existsById(id)) {
            inventory.setId(id);
            return inventoryRepository.save(inventory);
        } else {
            return null;
        }
    }

    @Override
    public void deleteInventory(Long id) {
        inventoryRepository.deleteById(id);
    }
}