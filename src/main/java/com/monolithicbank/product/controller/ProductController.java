package com.monolithicbank.product.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monolithicbank.product.entity.Product;
import com.monolithicbank.product.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.annotation.Resource;

@RestController
@RequestMapping("/product")
@Api(tags = "Product", description = "상품 관리 API")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    
    @Autowired
    @Resource(name = "productService")
    private ProductService productService;

    /**
     * 신규 계좌 상품 등록
     */
    @ApiOperation(value = "상품 등록", notes = "새로운 계좌 상품을 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
    @PostMapping("/")
    public ResponseEntity<String> createProduct(
            @ApiParam(value = "상품 정보", required = true)
            @RequestBody Product product) {
        try {
            productService.saveProduct(product);
            return ResponseEntity.ok("Product created successfully!");
        } catch (Exception e) {
            logger.error("Failed to create product: {}", e.getMessage(), e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to create product: " + e.getMessage());
        }
    }

    @ApiOperation(value = "상품 조회", notes = "상품ID로 상품 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> findProductById(
            @ApiParam(value = "상품ID", required = true)
            @PathVariable String id) {
        try {
            Product product = productService.findProductById(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            logger.error("Error occurred while fetching product with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 모든 계좌 상품 목록 조회
     */
    @ApiOperation(value = "상품 목록 조회", notes = "모든 계좌 상품 목록을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "상품이 없음"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
    @GetMapping("/")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            if (products.isEmpty()) {
                logger.warn("No products found in the database.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("Error occurred while fetching all products: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ApiOperation(value = "상품 삭제", notes = "상품ID로 상품을 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(
            @ApiParam(value = "상품ID", required = true)
            @PathVariable String id) {
        try {
            String result = productService.deleteProductById(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Failed to delete product with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to delete product: " + e.getMessage());
        }
    }

    /**
     * 특정 계좌 상품 수정
     */
    @ApiOperation(value = "상품 수정", notes = "특정 계좌 상품을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
    @PutMapping("/{id}")
    public ResponseEntity<String> updateProductById(
            @ApiParam(value = "상품 정보", required = true)
            @RequestBody Product product) {
        try {
            logger.info(">> product :" + product.getId() + " : " + product.getName());
            String result = productService.updateProductById(product);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("Failed to update product with id {}: {}", product.getId(), e.getMessage(), e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to update product: " + e.getMessage());
        }
    }
}