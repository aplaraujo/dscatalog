package com.example.dscatalog.utils;

import com.example.dscatalog.entities.Product;
import com.example.dscatalog.projections.ProductProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    // Retorna uma lista de produtos ordenada com base em uma lista desordenada
    public static List<Product> replace(List<ProductProjection> ordered, List<Product> unordered) {
        Map<Long, Product> map = new HashMap<>();
        for(Product obj:unordered) {
            map.put(obj.getId(), obj);
        }

        List<Product> result = new ArrayList<>();
        for(ProductProjection obj:ordered) {
            result.add(map.get(obj.getId()));
        }
        return result;
    }
}
