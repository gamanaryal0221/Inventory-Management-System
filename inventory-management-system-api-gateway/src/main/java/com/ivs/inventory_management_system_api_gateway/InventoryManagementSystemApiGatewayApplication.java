package com.ivs.inventory_management_system_api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryManagementSystemApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(InventoryManagementSystemApiGatewayApplication.class, args);
	}

}
