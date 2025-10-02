package com.example.dscatalog.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.dscatalog.dto.ProductDTO;
import com.example.dscatalog.entities.Product;
import com.example.dscatalog.repositories.ProductRepository;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> result = productRepository.findById(id);
        Product entity = result.orElseThrow(() -> new ResourceNotFoundException("Resource not found!"));
        ProductDTO dto = new ProductDTO(entity, entity.getCategories());
        return dto;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        List<Product> list = productRepository.findAll();
        List<ProductDTO> result = list.stream().map(prod -> new ProductDTO(prod)).toList();
        return result;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllByPage(String name, Pageable pageable) {
        Page<Product> products = productRepository.searchByName(name, pageable);
        return products.map(prod -> new ProductDTO(prod));
    }
}
