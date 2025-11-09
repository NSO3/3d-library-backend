// src/main/java/com/library/config/SecurityConfig.java
package com.library.config;

import com.library.security.JwtAuthenticationFilter;
import com.library.security.JwtTokenProvider;
import com.library.service.UserDetailsServiceImpl; 

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.RequiredArgsConstructor; // 💡 再度追加

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // 💡 コンストラクタでfinalフィールドを注入
public class SecurityConfig {

    // 💡 【修正】finalフィールドに戻す
    private final UserDetailsServiceImpl userDetailsService;

    // 1. パスワードエンコーダーの定義
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 3. 認証マネージャーをBeanとして公開
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    // 4. JwtAuthenticationFilterのBean定義（以前のロジックを使用）
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(
        JwtTokenProvider jwtTokenProvider
    ) {
        // 💡 フィルターはUserDetailsServiceImplも必要だが、ここではDIで解決されることを期待
        //    (もしDIでエラーが出る場合は、引数にUserDetailsServiceImplを追加)
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService); 
    }

    // 5. セキュリティフィルタチェーンの設定
    @Bean
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        JwtAuthenticationFilter jwtAuthenticationFilter 
    ) throws Exception {
        http
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/auth/**").permitAll() 
                .requestMatchers("/api/v1/books", "/api/v1/books/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())); 

        return http.build();
    }
    
    // 6. CORS設定
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173"); 
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}