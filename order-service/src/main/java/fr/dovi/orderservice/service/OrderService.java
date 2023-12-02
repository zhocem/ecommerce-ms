package fr.dovi.orderservice.service;

import fr.dovi.orderservice.dto.OrderLineItemDTO;
import fr.dovi.orderservice.dto.OrderRequest;
import fr.dovi.orderservice.model.Order;
import fr.dovi.orderservice.model.OrderLineItem;
import fr.dovi.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemDTOS().stream().map(this::mapToEntity).toList();
        order.setOrderLineItems(orderLineItems);

        orderRepository.save(order);
    }

    private OrderLineItem mapToEntity(OrderLineItemDTO orderLineItemDTO) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setPrice(orderLineItemDTO.getPrice());
        orderLineItem.setQuantity(orderLineItemDTO.getQuantity());
        orderLineItem.setSkuCode(orderLineItemDTO.getSkuCode());

        return orderLineItem;
    }
}
