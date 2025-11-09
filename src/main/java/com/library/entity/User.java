package com.library.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users") // テーブル名がuserは予約語の可能性があるためusersとする
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // ログインID

    @Column(nullable = false)
    private String password; // ハッシュ化されたパスワード

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // ユーザーの権限 (USER, ADMIN)
}