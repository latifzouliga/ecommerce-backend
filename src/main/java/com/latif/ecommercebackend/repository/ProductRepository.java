package com.latif.ecommercebackend.repository;

import com.latif.ecommercebackend.model.Product;
import org.springframework.data.repository.ListCrudRepository;

public interface ProductRepository extends ListCrudRepository<Product,Long> {
}
