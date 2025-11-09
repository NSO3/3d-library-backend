// src/main/java/com/library/controller/AuthController.java

package com.library.controller;

import com.library.dto.JwtAuthenticationResponse;
import com.library.dto.LoginRequest;
import com.library.dto.RegisterRequest; // 💡 DTOを使用
import com.library.entity.User;
import com.library.entity.Role;
import com.library.repository.UserRepository;
import com.library.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // 💡 /auth ではなく /api/auth に修正 (API設計の慣習)
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ----------------------------------------------------
    // 1. ユーザー新規登録 (POST /api/auth/register)
    // ----------------------------------------------------
    // 💡 Userエンティティではなく、RegisterRequest DTOを受け取るように修正
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegisterRequest registerRequest) {
        
        // 💡 ユーザー名が既に存在するかチェック
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
        }

        // 💡 新しいユーザーエンティティを作成
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        
        // パスワードをハッシュ化して保存
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        
        // 💡 認証方針に従い、JWTログインアカウントはADMINロールを付与
        user.setRole(Role.ADMIN); // UserエンティティのRolesフィールドに合わせて修正

        userRepository.save(user);

        return new ResponseEntity<>("User registered successfully as ADMIN", HttpStatus.OK);
    }

    // ----------------------------------------------------
    // 2. ログイン (認証) エンドポイント (POST /api/auth/login)
    // ----------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        
        // Spring Securityを使ってユーザーを認証
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 認証成功後、JWTトークンを生成
        String jwt = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
}