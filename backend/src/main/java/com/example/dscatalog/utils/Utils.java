package com.example.dscatalog.utils;

import com.example.dscatalog.entities.Product;
import com.example.dscatalog.projections.IdProjection;
import com.example.dscatalog.projections.ProductProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    // Método estático
    // Tipo genérico que retorna qualquer tipo de id
    // Retorna uma lista de IdProjection de qualquer tipo
    // Retorna uma lista ordenada e desordenada de qualquer tipo
    public static <ID> List<? extends IdProjection<ID>> replace(List<? extends IdProjection<ID>> ordered, List<? extends IdProjection<ID>> unordered) {
        Map<ID, IdProjection<ID>> map = new HashMap<>();
        for(IdProjection<ID> obj:unordered) {
            map.put(obj.getId(), obj);
        }

        List<IdProjection<ID>> result = new ArrayList<>();
        for(IdProjection<ID> obj:ordered) {
            result.add(map.get(obj.getId()));
        }
        return result;
    }
}
