// src/main/java/com/library/entity/Book.java

package com.library.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    
    private String title;
    private String author;
    private String color;
    private String summary;
    private String imageUrl;

    // ページリスト (一対多の関係)
    // mappedBy: Book側がPageのbookフィールドによってマッピングされていることを示す
    // CascadeType.ALL: Bookが操作されるとPageも連動して操作される
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Page> pages; 

    // BookとPage間の双方向参照を整合させるためのヘルパーメソッド（Lombokでは自動生成されない）
    public void addPage(Page page) {
        pages.add(page);
        page.setBook(this); // Page側にもBookを設定
    }

    // 更新時などに利用するヘルパーメソッド
    public void setPages(List<Page> pages) {
        this.pages = pages;
        if (pages != null) {
            pages.forEach(page -> page.setBook(this));
        }
    }
}