package com.latif.ecommercebackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.latif.ecommercebackend.enums.Rating;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "product_reviews")
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Rating rating;
    private String review;

    @ManyToOne
    @JsonBackReference
    public Product product;

}

//https://fakeapi.platzi.com/