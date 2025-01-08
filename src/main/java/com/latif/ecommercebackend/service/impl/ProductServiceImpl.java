package com.latif.ecommercebackend.service.impl;

import com.latif.ecommercebackend.model.Product;
import com.latif.ecommercebackend.repository.ProductRepository;
import com.latif.ecommercebackend.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
