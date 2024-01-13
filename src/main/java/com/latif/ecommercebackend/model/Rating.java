package com.latif.ecommercebackend.model;

import lombok.Getter;

@Getter
public enum Rating {
    ONE_STAR(1, "⭐"),
    TWO_STARS(2, "⭐⭐"),
    THREE_STARS(3, "⭐⭐⭐"),
    FOUR_STARS(4, "⭐⭐⭐⭐"),
    FIVE_STARS(5, "⭐⭐⭐⭐⭐");

    private final int numericValue;
    private final String emojiRepresentation;


    Rating(int numericValue, String emojiRepresentation) {
        this.numericValue = numericValue;
        this.emojiRepresentation = emojiRepresentation;
    }

}
