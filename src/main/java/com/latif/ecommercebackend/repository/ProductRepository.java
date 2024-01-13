package com.latif.ecommercebackend.repository;

import com.latif.ecommercebackend.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface ProductRepository extends ListCrudRepository<Product,Long> {

    @Query(value = """
            SELECT  * FROM product
            WHERE name = ?1
            ORDER BY name
            """,nativeQuery = true)
    List<Product> findByName(String name);
}
