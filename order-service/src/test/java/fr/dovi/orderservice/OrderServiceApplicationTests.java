package fr.dovi.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.dovi.orderservice.dto.OrderRequest;
import fr.dovi.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class OrderServiceApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private OrderRepository orderRepository;

    static final MySQLContainer MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer("mysql:latest");
        MY_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> MY_SQL_CONTAINER.getJdbcUrl());
        registry.add("spring.datasource.username", () -> MY_SQL_CONTAINER.getUsername());
        registry.add("spring.datasource.password", () -> MY_SQL_CONTAINER.getPassword());
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create");
    }

    @Test
    void shouldPlaceOrder() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderLineItemDTOS(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders.post(("/api/order"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest))
        ).andExpect(status().isCreated());

        Assertions.assertEquals(1, orderRepository.findAll().size());
    }

}
