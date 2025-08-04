package com.monolithicbank.product.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.monolithicbank.common.exception.BusinessException;
import com.monolithicbank.product.entity.Product;
import com.monolithicbank.product.repository.ProductRepository;

@Service("productService")
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * 계좌 상품 저장
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveProduct(Product product) throws Exception {
        if (product.getId() == null || product.getId().isEmpty()) {
            product.setId(UUID.randomUUID().toString());
        }
        productRepository.insertProduct(product);
    }

    /**
     * 특정 ID의 계좌 상품 조회
     */
    public Product findProductById(String id) throws Exception {
        Product product = productRepository.selectProduct(id);
        if (product == null) {
            throw new BusinessException("Product not found with id: " + id);
        }
        return product;
    }

    /**
     * 모든 계좌 상품 조회
     */
    public List<Product> getAllProducts() throws Exception {
        return productRepository.selectAllProducts();
    }

    /**
     * 계좌 상품 삭제
     */
    @Transactional(rollbackFor = Exception.class)
    public String deleteProductById(String id) throws Exception {
        Product existingProduct = productRepository.selectProduct(id);
        if (existingProduct == null) {
            throw new BusinessException("Product not found with id: " + id);
        }
        
        int result = productRepository.deleteProduct(id);
        if (result > 0) {
            return "Product deleted successfully";
        } else {
            throw new BusinessException("Failed to delete product");
        }
    }

    /**
     * 계좌 상품 수정
     */
    @Transactional(rollbackFor = Exception.class)
    public String updateProductById(Product product) throws Exception {
        Product existingProduct = productRepository.selectProduct(product.getId());
        if (existingProduct == null) {
            throw new BusinessException("Product not found with id: " + product.getId());
        }
        
        int result = productRepository.updateProduct(product);
        if (result > 0) {
            return "Product updated successfully";
        } else {
            throw new BusinessException("Failed to update product");
        }
    }
}