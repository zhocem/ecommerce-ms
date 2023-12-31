package com.dovi.inventoryservice;

import com.dovi.inventoryservice.model.Inventory;
import com.dovi.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
        return args -> {
            Inventory inventory = new Inventory();
            inventory.setSkuCode("S23_Ultra");
            inventory.setQuantity(12);

            Inventory inventory2 = new Inventory();
            inventory2.setSkuCode("Iphone15");
            inventory2.setQuantity(5);

            inventoryRepository.save(inventory);
            inventoryRepository.save(inventory2);

        };
    }

}
