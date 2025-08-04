package com.monolithicbank.user.controller;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.monolithicbank.user.entity.User;
import com.monolithicbank.user.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Api(tags = "User", description = "사용자 관리 API")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    @Resource(name = "userService")
    private UserService userService;

    /**
     * 사용자 등록
     */
    @ApiOperation(value = "사용자 등록", notes = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 400, message = "잘못된 요청"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @ApiParam(value = "사용자 정보", required = true)
            @RequestBody User user) {
        try {
            logger.info("User signup request for: {}", user.getUserId());
            int result = userService.createUser(user);
            if (result > 0) {
                return ResponseEntity.ok("User created successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to create user");
            }
        } catch (Exception e) {
            logger.error("Error during user signup: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

    /**
     * 사용자 로그인
     */
    @ApiOperation(value = "사용자 로그인", notes = "사용자 로그인을 처리하고 JWT 토큰을 발급합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "로그인 성공"),
        @ApiResponse(code = 401, message = "인증 실패"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @ApiParam(value = "로그인 정보 (user_id, password)", required = true)
            @RequestBody Map<String, String> loginRequest, 
                                       HttpServletResponse response) {
        try {
            String userId = loginRequest.get("user_id");
            String password = loginRequest.get("password");
            
            logger.info("Login attempt for user: {}", userId);
            
            boolean isValid = userService.validateUser(userId, password);
            if (!isValid) {
                logger.warn("Invalid credentials for user: {}", userId);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Invalid credentials");
            }
            
            // JWT 토큰 생성
            String token = userService.generateJWT(userId);
            
            // 쿠키에 JWT 토큰 설정
            Cookie jwtCookie = new Cookie("jwt", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setSecure(false); // HTTPS 환경에서는 true로 설정
            jwtCookie.setPath("/");
            jwtCookie.setMaxAge(24 * 60 * 60); // 24시간
            response.addCookie(jwtCookie);
            
            logger.info("Login successful for user: {}", userId);
            return ResponseEntity.ok("Login successful");
            
        } catch (Exception e) {
            logger.error("Error during login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal server error");
        }
    }

    /**
     * 사용자 정보 조회
     */
    @ApiOperation(value = "사용자 정보 조회", notes = "사용자ID로 사용자 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 404, message = "사용자를 찾을 수 없음")
    })

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(
            @ApiParam(value = "사용자ID", required = true)
            @PathVariable String userId) {
        try {
            User user = userService.retrieveUser(userId);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            logger.error("Error retrieving user {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * 사용자 존재 여부 확인
     */
    @ApiOperation(value = "사용자 존재 확인", notes = "사용자ID가 존재하는지 확인합니다.")
    @GetMapping("/{userId}/exists")
    public ResponseEntity<Boolean> existsUser(
            @ApiParam(value = "사용자ID", required = true)
            @PathVariable String userId) {
        try {
            boolean exists = userService.existsUserId(userId);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            logger.error("Error checking user existence {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 비밀번호 변경
     */
    @ApiOperation(value = "비밀번호 변경", notes = "사용자의 비밀번호를 변경합니다.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "성공"),
        @ApiResponse(code = 400, message = "잘못된 요청"),
        @ApiResponse(code = 500, message = "서버 오류")
    })
    @PostMapping("/{userId}/password")
    public ResponseEntity<String> changePassword(
            @ApiParam(value = "사용자ID", required = true)
            @PathVariable String userId, 
            @ApiParam(value = "비밀번호 변경 정보 (new_password)", required = true)
            @RequestBody Map<String, String> passwordRequest) {
        try {
            String newPassword = passwordRequest.get("new_password");
            int result = userService.changePassword(userId, newPassword);
            if (result > 0) {
                return ResponseEntity.ok("Password changed successfully");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to change password");
            }
        } catch (Exception e) {
            logger.error("Error changing password for user {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error: " + e.getMessage());
        }
    }

/**
 * 현재 인증된 사용자 정보 확인
 */
@ApiOperation(value = "인증 상태 확인", notes = "현재 인증된 사용자의 정보를 반환합니다.")
@GetMapping("/check")
public ResponseEntity<User> checkAuth() {
    try {
        // JWT 토큰에서 사용자 ID 추출 로직 (실제 구현은 인증 방식에 따라 다름)
        String userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        User user = userService.retrieveUser(userId);
        return ResponseEntity.ok(user);
    } catch (Exception e) {
        logger.error("Error checking authentication: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

// 현재 인증된 사용자 ID를 가져오는 메서드 (실제 구현은 인증 방식에 따라 다름)
private String getCurrentUserId() {
    // JWT 토큰에서 사용자 ID 추출 로직
    // ...
    return null; // 실제 구현에서는 사용자 ID 반환
}

}