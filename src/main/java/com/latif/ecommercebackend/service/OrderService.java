package com.latif.ecommercebackend.service;

import com.latif.ecommercebackend.model.User;
import com.latif.ecommercebackend.model.WebOrder;

import java.util.List;

public interface OrderService {

    List<WebOrder> getOrders(User user);
}
