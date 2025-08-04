package com.monolithicbank.common.controller;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Health check controller for monitoring application status
 */
@RestController
@RequestMapping("/health")
@Api(tags = "Health", description = "애플리케이션 상태 모니터링 API")
public class HealthController {
    
    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);
    
    @Autowired
    private DataSource dataSource;
    
    /**
     * Basic health check endpoint
     */
    @ApiOperation(value = "기본 상태 확인", notes = "애플리케이션의 기본 상태를 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "애플리케이션 정상 작동")
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("application", "Monolithic Bank");
        health.put("version", "1.0.0");
        health.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(health);
    }
    
    /**
     * Detailed health check with database connectivity
     */
    @ApiOperation(value = "상세 상태 확인", notes = "데이터베이스 연결 상태를 포함한 상세 상태를 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "상세 상태 정보")
    })
    @GetMapping("/detailed")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        Map<String, Object> health = new HashMap<>();
        Map<String, Object> components = new HashMap<>();
        
        // Application status
        health.put("status", "UP");
        health.put("application", "Monolithic Bank");
        health.put("version", "1.0.0");
        health.put("timestamp", System.currentTimeMillis());
        
        // Database connectivity check
        Map<String, Object> database = new HashMap<>();
        try {
            // Test database connection
            dataSource.getConnection().close();
            database.put("status", "UP");
            database.put("database", "PostgreSQL");
        } catch (Exception e) {
            logger.error("Database health check failed", e);
            database.put("status", "DOWN");
            database.put("error", e.getMessage());
            health.put("status", "DOWN");
        }
        components.put("database", database);
        
        // JVM information
        Map<String, Object> jvm = new HashMap<>();
        Runtime runtime = Runtime.getRuntime();
        jvm.put("status", "UP");
        jvm.put("maxMemory", runtime.maxMemory());
        jvm.put("totalMemory", runtime.totalMemory());
        jvm.put("freeMemory", runtime.freeMemory());
        jvm.put("usedMemory", runtime.totalMemory() - runtime.freeMemory());
        components.put("jvm", jvm);
        
        health.put("components", components);
        
        return ResponseEntity.ok(health);
    }
    
    /**
     * Readiness probe for Kubernetes/container orchestration
     */
    @ApiOperation(value = "준비 상태 확인", notes = "애플리케이션이 요청을 처리할 준비가 되었는지 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "준비 완료"),
        @ApiResponse(code = 503, message = "준비 중")
    })
    @GetMapping("/ready")
    public ResponseEntity<Map<String, String>> ready() {
        Map<String, String> status = new HashMap<>();
        
        try {
            // Check if application is ready to serve requests
            dataSource.getConnection().close();
            status.put("status", "READY");
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            logger.error("Readiness check failed", e);
            status.put("status", "NOT_READY");
            status.put("reason", e.getMessage());
            return ResponseEntity.status(503).body(status);
        }
    }
    
    /**
     * Liveness probe for Kubernetes/container orchestration
     */
    @ApiOperation(value = "생존 상태 확인", notes = "애플리케이션이 살아있는지 확인합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "애플리케이션 생존")
    })
    @GetMapping("/live")
    public ResponseEntity<Map<String, String>> live() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "ALIVE");
        return ResponseEntity.ok(status);
    }
}