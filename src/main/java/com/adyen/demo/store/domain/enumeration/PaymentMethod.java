package com.adyen.demo.store.domain.enumeration;

/**
 * The PaymentMethod enumeration.
 */
public enum PaymentMethod {
    CREDIT_CARD("scheme"),
    IDEAL("ideal");

    private final String value;


    PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
