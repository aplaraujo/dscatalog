package com.example.dscatalog.repositories;

import com.example.dscatalog.entities.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {
        // Arrange
        long existingId = 1L;

        // Act
        productRepository.deleteById(existingId);
        Optional<Product> result = productRepository.findById(existingId);

        // Assert
        Assertions.assertFalse(result.isPresent());
    }
}
