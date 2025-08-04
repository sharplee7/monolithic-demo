package com.monolithicbank.product.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.monolithicbank.product.entity.Product;

@Mapper
public interface ProductRepository {
    int insertProduct(Product product) throws Exception;
    Product selectProduct(String id) throws Exception;
    List<Product> selectAllProducts() throws Exception;
    int updateProduct(Product product) throws Exception;
    int deleteProduct(String id) throws Exception;
}