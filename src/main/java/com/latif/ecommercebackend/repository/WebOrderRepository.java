package com.latif.ecommercebackend.repository;

import com.latif.ecommercebackend.model.LocalUser;
import com.latif.ecommercebackend.model.WebOrder;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface WebOrderRepository extends ListCrudRepository<WebOrder,Long> {

    List<WebOrder> findByUser(LocalUser user);
}
