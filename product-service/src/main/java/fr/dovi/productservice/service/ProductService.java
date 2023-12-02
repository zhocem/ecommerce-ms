package fr.dovi.productservice.service;

import fr.dovi.productservice.dto.ProductRequest;
import fr.dovi.productservice.dto.ProductResponse;
import fr.dovi.productservice.model.Product;
import fr.dovi.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);

        log.info("Product {} is saved successfully", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> productList = productRepository.findAll();

        // Mapping Product to ProductResponse
        // We could use MapStruct
        List<ProductResponse> productResponseList = productList.stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());

        return productResponseList;
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
