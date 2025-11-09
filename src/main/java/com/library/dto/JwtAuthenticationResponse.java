// src/main/java/com/library/dto/JwtAuthenticationResponse.java

package com.library.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

// 認証成功時にサーバーが返すレスポンスの構造
@Data
@NoArgsConstructor
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";

// 💡 【修正点】String (JWT) のみを受け取るコンストラクタを手動で定義
    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
        // tokenType はデフォルト値 "Bearer" が使用される
    }

}