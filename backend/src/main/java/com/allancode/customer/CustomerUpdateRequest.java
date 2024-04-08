package com.allancode.customer;

public record CustomerUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
