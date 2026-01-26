package com.example.spring_boot_api_jwt_ad.repository;

import com.example.spring_boot_api_jwt_ad.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
