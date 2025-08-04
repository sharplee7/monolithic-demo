package com.monolithicbank.user.service;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.monolithicbank.common.exception.BusinessException;
import io.jsonwebtoken.security.Keys;
import com.monolithicbank.user.entity.User;
import com.monolithicbank.user.repository.UserRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service("userService")
public class UserService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * 사용자 등록
     */
    @Transactional(rollbackFor = Exception.class)
    public int createUser(User user) throws Exception {
        // 사용자 ID 중복 확인
        if (existsUserId(user.getUserId())) {
            throw new BusinessException("User ID already exists: " + user.getUserId());
        }
        
        // Salt 생성
        String salt = generateSalt();
        user.setSalt(salt);
        
        // 비밀번호 해시화
        String hashedPassword = hashPassword(user.getPasswordHash(), salt);
        user.setPasswordHash(hashedPassword);
        
        // 기본 상태 설정
        if (user.getStatus() == null) {
            user.setStatus("ACTIVE");
        }
        
        return userRepository.insertUser(user);
    }

    /**
     * 사용자 로그인 검증
     */
    public boolean validateUser(String userId, String password) throws Exception {
        User user = userRepository.selectUser(userId);
        if (user == null) {
            return false;
        }
        
        String saltedPassword = password + user.getSalt();
        return passwordEncoder.matches(saltedPassword, user.getPasswordHash());
    }

    /**
     * JWT 토큰 생성
     */
    public String generateJWT(String userId) throws Exception {
        // 안전한 키 생성 (HS256 알고리즘에 적합한 256비트 이상의 키)
        Key key;
        try {
            // 기존 시크릿을 시드로 사용하여 일관된 키 생성
            byte[] keyBytes = jwtSecret.getBytes();
            // 키 길이가 충분하지 않으면 확장
            if (keyBytes.length < 32) { // 256 bits = 32 bytes
                byte[] newKeyBytes = new byte[32];
                System.arraycopy(keyBytes, 0, newKeyBytes, 0, Math.min(keyBytes.length, 32));
                // 나머지 바이트를 채움
                for (int i = keyBytes.length; i < 32; i++) {
                    newKeyBytes[i] = (byte) (i % 256);
                }
                key = new SecretKeySpec(newKeyBytes, SignatureAlgorithm.HS256.getJcaName());
            } else {
                key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
            }
        } catch (Exception e) {
            // 예외 발생 시 안전한 새 키 생성
            key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
        
        return Jwts.builder()
                .setSubject(userId)
                .claim("user_id", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 24시간
                .signWith(key)
                .compact();
    }

    /**
     * 사용자 정보 조회
     */
    public User retrieveUser(String userId) throws Exception {
        User user = userRepository.selectUser(userId);
        if (user == null) {
            throw new BusinessException("User not found: " + userId);
        }
        // 보안을 위해 비밀번호 해시와 솔트는 제거
        user.setPasswordHash(null);
        user.setSalt(null);
        return user;
    }

    /**
     * 비밀번호 변경
     */
    @Transactional(rollbackFor = Exception.class)
    public int changePassword(String userId, String newPassword) throws Exception {
        User user = userRepository.selectUser(userId);
        if (user == null) {
            throw new BusinessException("User not found: " + userId);
        }
        
        // 새로운 Salt 생성
        String salt = generateSalt();
        String hashedPassword = hashPassword(newPassword, salt);
        
        User updateUser = User.builder()
                .userId(userId)
                .passwordHash(hashedPassword)
                .salt(salt)
                .build();
                
        return userRepository.updatePassword(updateUser);
    }

    /**
     * 사용자 ID 존재 여부 확인
     */
    public boolean existsUserId(String userId) throws Exception {
        return userRepository.existsUser(userId) > 0;
    }

    /**
     * Salt 생성
     */
    private String generateSalt() {
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * 비밀번호 해시화
     */
    private String hashPassword(String password, String salt) {
        String saltedPassword = password + salt;
        return passwordEncoder.encode(saltedPassword);
    }
}