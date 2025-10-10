package com.example.dscatalog.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.dscatalog.projections.ProductProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.dscatalog.dto.CategoryDTO;
import com.example.dscatalog.dto.ProductDTO;
import com.example.dscatalog.entities.Category;
import com.example.dscatalog.entities.Product;
import com.example.dscatalog.repositories.CategoryRepository;
import com.example.dscatalog.repositories.ProductRepository;
import com.example.dscatalog.services.exceptions.DatabaseException;
import com.example.dscatalog.services.exceptions.ResourceNotFoundException;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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
    public Page<ProductDTO> findAllByPage(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(prod -> new ProductDTO(prod));
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(long id, ProductDTO dto) {
        Product entity = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        copyDtoToEntity(dto, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional(propagation=Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource not found!");
        }

        try {
            productRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Referencial integrity failure");
        }
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
        entity.setDate(dto.getDate());

        entity.getCategories().clear();
        for (CategoryDTO catDTO : dto.getCategories()) {
            Category cat = categoryRepository.getReferenceById(catDTO.getId());
            entity.getCategories().add(cat);
        }
    }

    @Transactional(readOnly = true)
    public Page<ProductProjection> findAllPaged(String name, String categoryId, Pageable pageable) {
        List<Long> categoryIds = List.of();
        if (!"0".equals(categoryId)) {
            categoryIds = Arrays.stream(categoryId.split(",")).map(Long::parseLong).toList();
        }
        return productRepository.searchProducts(categoryIds, name, pageable);
    }
}
