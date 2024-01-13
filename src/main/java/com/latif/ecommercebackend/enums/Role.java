package com.latif.ecommercebackend.enums;

import lombok.Getter;

@Getter
public enum Role {

    ADMIN("Admin"),
    MANAGER("Manager"),
    CUSTOMER("Customer");


    private final String value;

    Role(String value) {
    this.value = value;
    }
}
