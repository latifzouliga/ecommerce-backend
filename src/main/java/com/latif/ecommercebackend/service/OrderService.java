package com.latif.ecommercebackend.service;

import com.latif.ecommercebackend.model.LocalUser;
import com.latif.ecommercebackend.model.WebOrder;

import java.util.List;

public interface OrderService {

    List<WebOrder> getOrders(LocalUser user);
}
