// src/main/java/com/library/controller/AdminController.java

package com.library.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Spring SecurityConfigと一致するベースパス
@RequestMapping("/api/v1/admin") 
@RestController
public class AdminController {

    // SecurityConfigで定義したロールチェックを、メソッドレベルでも実行 (二重チェック)
    // SecurityConfigの設定が正しければ、このアノテーションは不要だが、より明確になる
    @PreAuthorize("hasRole('ADMIN')") 
    @GetMapping("/test")
    public ResponseEntity<String> testAdminAccess() {
        return ResponseEntity.ok("Success! You have ROLE_ADMIN access.");
    }

    // 他の管理者APIはここに追加
}