package com.monolithicbank.user.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class User {
    private String userId;
    private String passwordHash;
    private String salt;
    private String email;
    private String name;
    private String createdDate;
    private String updatedDate;
    private String status;

    @Builder
    public User(String userId, String passwordHash, String salt, String email, String name, 
                String createdDate, String updatedDate, String status) {
        this.userId = userId;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.email = email;
        this.name = name;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.status = status;
    }
}