package com.example.dscatalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.dscatalog.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Busca paginada
    @Query("SELECT obj FROM Category obj WHERE UPPER(obj.name) LIKE UPPER(CONCAT('%', :name, '%'))")
    Page<Category> searchByName(String name, Pageable pageable);

}
