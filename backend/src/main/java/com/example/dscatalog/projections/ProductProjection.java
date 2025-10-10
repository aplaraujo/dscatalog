package com.example.dscatalog.projections;

// Dados que serão retornados na consulta
public interface ProductProjection extends IdProjection<Long>{
    String getName();
}
