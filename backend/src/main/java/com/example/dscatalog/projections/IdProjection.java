package com.example.dscatalog.projections;

// Interface genérica para vários tipos de ID
public interface IdProjection<E> {
    E getId();
}