package com.monolithicbank.user.repository;

import org.apache.ibatis.annotations.Mapper;

import com.monolithicbank.user.entity.User;

@Mapper
public interface UserRepository {
    int insertUser(User user) throws Exception;
    User selectUser(String userId) throws Exception;
    int updateUser(User user) throws Exception;
    int deleteUser(String userId) throws Exception;
    int existsUser(String userId) throws Exception;
    int updatePassword(User user) throws Exception;
}