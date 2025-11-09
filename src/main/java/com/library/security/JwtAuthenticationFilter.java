// src/main/java/com/library/security/JwtAuthenticationFilter.java

package com.library.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.library.service.UserDetailsServiceImpl; // 💡 以前作成したサービス

import java.io.IOException;
import jakarta.annotation.Nonnull;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    // ------------------------------------------
    // 1. リクエストごとに認証処理を実行
    // ------------------------------------------
    // 💡 親クラスの@NonNullに関するIDEの警告を抑制
    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain)
            throws ServletException, IOException {
        
        try {
            String jwt = getJwtFromRequest(request);
            
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                
                // 1. トークンからユーザー名を取得
                String username = tokenProvider.getUsernameFromToken(jwt);

                // 2. ユーザー名からユーザー詳細情報をロード
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                
                // 3. 認証オブジェクトを生成
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                // 4. リクエストの詳細情報を設定
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 5. Spring Security Contextに認証情報を設定
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        // 💡 フィルターチェーンを継続 (次のフィルターまたはコントローラーへ)
        filterChain.doFilter(request, response);
    }

    // ------------------------------------------
    // 2. ヘルパーメソッド: リクエストヘッダーからトークンを抽出
    // ------------------------------------------
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // "Bearer " で始まっていれば、トークン部分を切り出す
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " の7文字をスキップ
        }
        return null;
    }
}
