package com.latif.ecommercebackend.controller;

import com.latif.ecommercebackend.model.LocalUser;
import com.latif.ecommercebackend.model.ResponseWrapper;
import com.latif.ecommercebackend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {


    private final OrderService orderService;


    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @GetMapping
    public ResponseEntity<ResponseWrapper> getOrders(@AuthenticationPrincipal LocalUser user){
        return ResponseEntity.ok(
                ResponseWrapper.builder()
                        .status(HttpStatus.OK)
                        .success(true)
                        .message("Orders for %s %s retrieved successfully".formatted(user.getFirstName(),user.getLastName()))
                        .data(orderService.getOrders(user))
                        .build()
        );
    }










}
