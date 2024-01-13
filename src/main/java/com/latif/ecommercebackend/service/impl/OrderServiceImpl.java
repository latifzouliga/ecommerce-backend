package com.latif.ecommercebackend.service.impl;

import com.latif.ecommercebackend.model.LocalUser;
import com.latif.ecommercebackend.model.WebOrder;
import com.latif.ecommercebackend.repository.WebOrderRepository;
import com.latif.ecommercebackend.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final WebOrderRepository webOrderRepository;

    public OrderServiceImpl(WebOrderRepository webOrderRepository) {
        this.webOrderRepository = webOrderRepository;
    }

    @Override
    public List<WebOrder> getOrders(LocalUser user) {
        return webOrderRepository.findByUser(user);
    }
}
