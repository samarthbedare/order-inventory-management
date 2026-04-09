package com.project.orderinventorymanagement.productservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.orderinventorymanagement.productservice.dto.ProductDTO;
import com.project.orderinventorymanagement.productservice.dto.ProductUpdateDTO;
import com.project.orderinventorymanagement.productservice.entity.Product;
import com.project.orderinventorymanagement.productservice.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> getAllProducts(String brand, String colour, String size) {
        List<Product> products =
                productRepository.findByBrandContainingIgnoreCaseAndColourContainingIgnoreCaseAndSizeContainingIgnoreCase(
                        brand == null ? "" : brand,
                        colour == null ? "" : colour,
                        size == null ? "" : size
                );

        return products.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public ProductDTO getProductById(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        return product != null ? convertToDTO(product) : null;
    }

    public ProductDTO addProduct(ProductDTO dto) {
        Product product = convertToEntity(dto);
        Product saved = productRepository.save(product);
        return convertToDTO(saved);
    }

    public ProductDTO updateProduct(Integer id, ProductUpdateDTO dto) {
        Product existing = productRepository.findById(id).orElse(null);

        if (existing != null) {
            if (dto.getUnitPrice() != null) {
                existing.setUnitPrice(dto.getUnitPrice());
            }

            if (dto.getRating() != null) {
                existing.setRating(dto.getRating());
            }

            Product updated = productRepository.save(existing);
            return convertToDTO(updated);
        }

        return null;
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

