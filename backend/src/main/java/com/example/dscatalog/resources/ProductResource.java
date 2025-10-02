package com.example.dscatalog.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dscatalog.dto.ProductDTO;
import com.example.dscatalog.services.ProductService;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO dto = productService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(
            @RequestParam(name = "name", defaultValue = "") String name, Pageable pageable,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "moment") String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction)
            {
        Page<ProductDTO> dto = productService.findAllByPage(name, pageable);
        return ResponseEntity.ok(dto);
    }
}
