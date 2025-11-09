// src/main/java/com/library/dto/LoginRequest.java

package com.library.dto;

import lombok.Data;

// 認証時にユーザーが送るリクエストボディの構造
@Data
public class LoginRequest {
    private String username;
    private String password;
}