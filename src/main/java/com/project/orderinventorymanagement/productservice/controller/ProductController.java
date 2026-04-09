package com.project.orderinventorymanagement.productservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.project.orderinventorymanagement.productservice.dto.ProductDTO;
import com.project.orderinventorymanagement.productservice.dto.ProductUpdateDTO;
import com.project.orderinventorymanagement.productservice.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductDTO> getAllProducts(
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String colour,
            @RequestParam(required = false) String size) {

        return productService.getAllProducts(brand, colour, size);
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Integer id) {
        return productService.getProductById(id);
    }

    @PostMapping
    public ProductDTO addProduct(@RequestBody ProductDTO dto) {
        return productService.addProduct(dto);
    }

    @PatchMapping("/{id}")
    public ProductDTO updateProduct(
            @PathVariable Integer id,
            @RequestBody ProductUpdateDTO dto) {

        return productService.updateProduct(id, dto);
    }
}
