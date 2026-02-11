package com.balicki.flashsales;

import com.balicki.flashsales.orders.OrderRequest;
import com.balicki.flashsales.products.Product;
import com.balicki.flashsales.products.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
public class OrderIntegrationTest {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void init() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldCreateOrderUsingImportedContainers() {
        Product existingProduct = productRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Product Not Found"));

        int initialStock = existingProduct.getStock();
        OrderRequest request = new OrderRequest(existingProduct.getId(), 2);

        ResponseEntity<String> response = restClient.post()
                .uri("/orders")
                .body(request)
                .retrieve()
                .toEntity(String.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
            Product updatedProduct = productRepository.findById(existingProduct.getId()).orElseThrow();

            assertThat(updatedProduct.getStock()).isEqualTo(initialStock - 2);
        });
    }
}
