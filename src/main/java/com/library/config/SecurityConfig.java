// src/main/java/com/library/config/SecurityConfig.java

package com.library.config;

//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;

// Securityの設定は必須ではないため、依存関係に追加されていない場合はスキップ
// もしSpring Securityの依存関係が既に入っている場合はこの設定が必要
/*
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // CSRF保護を無効化（API開発では一般的）
            // 💡 H2コンソールへのアクセスを認証なしで許可する
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll() 
                .requestMatchers("/api/v1/books/**").permitAll() // 全てのAPIも一時的に許可
                .anyRequest().authenticated()
            );

        // H2コンソールを使用するためにフレームオプションを無効化（IFrame対策）
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}
*/