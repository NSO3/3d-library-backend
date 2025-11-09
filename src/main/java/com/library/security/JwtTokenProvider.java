package com.library.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.library.config.AppProperties;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger; // 💡 1. Loggerのimportを追加
import org.slf4j.LoggerFactory; // 💡 2. LoggerFactoryのimportを追加

@Component
public class JwtTokenProvider {

    // クラス内にロガーインスタンスを定義
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    // @Valueは削除し、finalフィールドとして定義
    private final String jwtSecret;
    private final long jwtExpirationMs; // 💡 型をAppPropertiesに合わせてlongに修正


    // 💡 コンストラクタインジェクションに変更
    public JwtTokenProvider(AppProperties appProperties) {
        // AppPropertiesから値を取得
        this.jwtSecret = appProperties.getJwtSecret();
        this.jwtExpirationMs = appProperties.getJwtExpirationMs(); // AppPropertiesのフィールド名に合わせて修正
        logger.info("🔑 [INIT] Current loaded JWT Secret ({} chars): {}", 
            this.jwtSecret.length(), this.jwtSecret.substring(0, 10) + "..."); //
    }

    // ------------------------------------------
    // 1. トークン生成ロジック
    // ------------------------------------------
    public String generateToken(Authentication authentication) {
        // 認証オブジェクトからユーザー名を取得
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        // 💡 【修正】権限リストを取得し、カンマ区切りの文字列にする
        String roles = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // ユーザー名を主体とする
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .claim("roles", roles)
                .signWith(key(), SignatureAlgorithm.HS512) // HMAC SHA-512で署名
                .compact();
    }

    private Key key() {
        // 💡 実行時のキー値をログ出力 (不一致を確認するため)
        logger.debug("🔑 [KEY_GEN] Secret used for signing/verification: {}", 
            jwtSecret.substring(0, 10) + "...");
        // Base64形式のシークレットキーをデコードしてKeyオブジェクトを生成
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // ------------------------------------------
    // 2. トークン検証ロジック
    // ------------------------------------------
    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.getMessage());
            return false; 
        }
    }

    // ------------------------------------------
    // 3. トークンからユーザー名を取得
    // ------------------------------------------
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }
}