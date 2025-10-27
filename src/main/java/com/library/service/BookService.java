// src/main/java/com/library/service/BookService.java

package com.library.service;

import com.library.entity.Book;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // 💡 ビジネスロジックを担うService層であることを示す
@RequiredArgsConstructor // 💡 finalフィールドを自動でコンストラクタインジェクションする
public class BookService {

    private final BookRepository bookRepository; // DB操作のためのリポジトリを注入

    // 1. 本の作成 (CreateBookPageからPOSTされる)
    @Transactional
    public Book saveBook(Book book) {
        // 💡 ここに、タイトルや概要に対するバリデーション、
        //    将来的な精査（モデレーション）ロジックを挿入可能
        
        // 双方向参照の整合性を確保（エンティティ側でヘルパーメソッドを設定済み）
        book.getPages().forEach(page -> page.setBook(book));
        
        return bookRepository.save(book);
    }

    // 2. 全本のメタデータ取得 (3D図書館の棚表示用)
    @Transactional(readOnly = true)
    public List<Book> findAllMetadata() {
        // 💡 本棚表示に必要な最低限のデータのみを返す（ページのコンテンツは不要）
        // この段階では全てを返すが、最終的にはDTO (Data Transfer Object) を使用して最適化推奨
        return bookRepository.findAll();
    }

    // 3. 本詳細の取得 (BookDetailPage用)
    @Transactional(readOnly = true)
    public Book findById(Long id) {
        // 💡 IDが見つからない場合は例外をスロー
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    // 4. 本の検索 (SearchPage用)
    @Transactional(readOnly = true)
    public List<Book> search(String query) {
        // 💡 リポジトリで定義したカスタム検索メソッドを使用
        return bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
    }
}