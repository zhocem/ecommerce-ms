package fr.dovi.orderservice.feign;

import fr.dovi.orderservice.dto.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "inventoryClient", url = "INVENTORY-SERVICE", path = "/api/inventory")
public interface InventoryClient {

    @GetMapping
    List<InventoryResponse> isInStock(@RequestParam("skuCode") List<String> skuCodes);
}
