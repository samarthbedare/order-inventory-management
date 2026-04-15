package com.project.orderinventorymanagement.productservice.service;

import com.project.orderinventorymanagement.productservice.dto.ProductDTO;
import com.project.orderinventorymanagement.productservice.dto.ProductUpdateDTO;
import com.project.orderinventorymanagement.productservice.entity.Product;
import com.project.orderinventorymanagement.productservice.exception.ProductNotFoundException;
import com.project.orderinventorymanagement.productservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> getAllProducts(String name, String brand, String colour, String size) {
        List<Product> products =
                productRepository.findByProductNameContainingIgnoreCaseAndBrandContainingIgnoreCaseAndColourContainingIgnoreCaseAndSizeContainingIgnoreCase(
                        name == null ? "" : name,
                        brand == null ? "" : brand,
                        colour == null ? "" : colour,
                        size == null ? "" : size
                );

        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ProductDTO getProductById(Integer id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));

        return convertToDTO(product);
    }

    public ProductDTO addProduct(ProductDTO dto) {

        if (dto.getProductName() == null || dto.getProductName().isBlank()) {
            throw new IllegalArgumentException("Product name must not be null or empty");
        }
        if (dto.getUnitPrice() == null || dto.getUnitPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Unit price must be greater than 0");
        }
        if (dto.getRating() != null && (dto.getRating() < 1 || dto.getRating() > 5)) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        Product product = convertToEntity(dto);
        Product saved = productRepository.save(product);
        return convertToDTO(saved);
    }

    public ProductDTO updateProduct(Integer id, ProductUpdateDTO dto) {

        Product existing = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product not found with id: " + id));

        if (dto.getUnitPrice() != null) {
            existing.setUnitPrice(dto.getUnitPrice());
        }

        if (dto.getRating() != null) {
            existing.setRating(dto.getRating());
        }

        Product updated = productRepository.save(existing);
        return convertToDTO(updated);
    }

    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Cannot delete: Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    private ProductDTO convertToDTO(Product product) {
        ProductDTO dto = new ProductDTO();

        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());
        dto.setUnitPrice(product.getUnitPrice());
        dto.setColour(product.getColour());
        dto.setBrand(product.getBrand());
        dto.setSize(product.getSize());
        dto.setRating(product.getRating());

        return dto;
    }

    private Product convertToEntity(ProductDTO dto) {
        Product product = new Product();

        product.setProductId(dto.getProductId());
        product.setProductName(dto.getProductName());
        product.setUnitPrice(dto.getUnitPrice());
        product.setColour(dto.getColour());
        product.setBrand(dto.getBrand());
        product.setSize(dto.getSize());
        product.setRating(dto.getRating());

        return product;
    }
}
