package fr.dovi.orderservice.service;

import fr.dovi.orderservice.dto.InventoryResponse;
import fr.dovi.orderservice.dto.OrderLineItemDTO;
import fr.dovi.orderservice.dto.OrderRequest;
import fr.dovi.orderservice.feign.InventoryClient;
import fr.dovi.orderservice.model.Order;
import fr.dovi.orderservice.model.OrderLineItem;
import fr.dovi.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient webClient;

    @Autowired
    private InventoryClient inventoryClient;


    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemDTOS().stream().map(this::mapToEntity).toList();
        order.setOrderLineItems(orderLineItems);

        List<String> skuCodes = order.getOrderLineItems().stream().map(OrderLineItem::getSkuCode).toList();

        List<InventoryResponse> inventoryResponseList = inventoryClient.isInStock(skuCodes);

        InventoryResponse[] inventoryResponses = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();


        boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock, please try again later !");
        }
    }

    private OrderLineItem mapToEntity(OrderLineItemDTO orderLineItemDTO) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(orderLineItemDTO.getPrice());
        orderLineItem.setQuantity(orderLineItemDTO.getQuantity());
        orderLineItem.setSkuCode(orderLineItemDTO.getSkuCode());

        return orderLineItem;
    }

    public List<Order> getAllOrders() {
        List<Order> orderList = orderRepository.findAll();
        return orderList;
    }
}
