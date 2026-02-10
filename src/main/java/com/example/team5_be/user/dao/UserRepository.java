package com.example.team5_be.user.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.team5_be.user.domain.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity,String>{

    Optional<UserEntity> findByUserId(String userId);

    // 아이디 + 이름으로 회원 조회
    Optional<UserEntity> findByUserIdAndUserName(String userId, String userName);

}
