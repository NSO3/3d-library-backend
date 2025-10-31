// src/main/java/com/library/entity/Page.java

package com.library.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data // Getter, Setter, toString, equals/hashCode を自動生成
@NoArgsConstructor
@AllArgsConstructor
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pageId;
    
    private int pageNumber; 
    
    @Lob // Large Object: 長文のテキストを格納
    private String content;

    // ページが属する本 (多対一の関係)
    @JsonBackReference // 💡 【修正】JSONの子側 (親への逆参照を停止)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
}