package com.example.dscatalog.tests.factory;

import com.example.dscatalog.dto.ProductDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {
    public static Product createProduct() {
        Product product = new Product(1L, "PS5", "The new generation PS5 video game", 700.00, "https://img.com/img.png", Instant.parse("2020-07-20T10:00:00Z"));
        product.getCategories().add(new Category(2L, "Eletronicos"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
