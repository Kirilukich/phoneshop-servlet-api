package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundException;
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);
    List<Product> findProducts(String query, BigDecimal minPrice, BigDecimal maxPrice, SearchMethod searchMethod);
    void save(Product product);
    void delete(Long id);
}
