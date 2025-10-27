// src/main/java/com/library/repository/BookRepository.java

package com.library.repository;

import com.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    
    // 💡 検索機能用に、タイトルまたは著者名で検索するカスタムメソッドを定義
    List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
}