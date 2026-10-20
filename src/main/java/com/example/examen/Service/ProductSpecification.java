package com.example.examen.specification;

import com.example.examen.Model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    // Filtrar por nombre (coercción parcial / ignore case)
    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null || name.isEmpty() ? null :
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    // Filtrar por ID de categoría
    public static Specification<Product> hasCategoryId(Long categoryId) {
        return (root, query, criteriaBuilder) ->
                categoryId == null ? null :
                        criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    // Filtrar por precio mínimo
    public static Specification<Product> minPrice(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) ->
                minPrice == null ? null :
                        criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    // Filtrar por precio máximo
    public static Specification<Product> maxPrice(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) ->
                maxPrice == null ? null :
                        criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }
}