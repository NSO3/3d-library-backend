package com.library.repository;

import com.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 💡 Spring Securityがユーザーをロードするために必要
    Optional<User> findByUsername(String username);

    // 💡 【追加】ユーザー名が存在するかどうかをチェックするメソッド
    Boolean existsByUsername(String username);
}