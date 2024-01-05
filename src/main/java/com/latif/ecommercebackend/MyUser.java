package com.latif.ecommercebackend;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class MyUser {

    @Id
    private Long id;
    private String name;
}
