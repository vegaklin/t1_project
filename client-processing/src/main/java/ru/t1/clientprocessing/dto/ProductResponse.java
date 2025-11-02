package ru.t1.clientprocessing.dto;

import ru.t1.clientprocessing.model.ProductKey;

import java.time.LocalDate;

public record ProductResponse (
        Long id,
        String name,
        ProductKey key,
        LocalDate createDate,
        String productId
){
}
