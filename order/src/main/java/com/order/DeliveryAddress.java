package com.order;

import jakarta.persistence.Embeddable;

@Embeddable
public record DeliveryAddress(String city, String postcode, String street, String houseNumber) {
}
