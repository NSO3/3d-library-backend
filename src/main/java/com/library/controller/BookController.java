// src/main/java/com/library/controller/BookController.java

package com.library.controller;

import com.library.entity.Book;
import com.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

// 💡 【追加】フロントエンドのオリジンからのアクセスを許可
@CrossOrigin(origins = "http://localhost:5173")
@RestController // 💡 RESTful APIのコントローラーであることを示す
@RequestMapping("/api/v1/books") // 💡 すべてのエンドポイントのベースパス
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService; // Service層を注入

    // 1. 本の作成: POST /api/v1/books
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // HTTP 201 Created を返す
    public Book createBook(@RequestBody Book book) {
        // Service層を経由してDBに保存
        return bookService.saveBook(book);
    }

    // 2. 全本のメタデータ取得: GET /api/v1/books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.findAllMetadata();
    }

    // 3. 本詳細の取得: GET /api/v1/books/{id}
    @GetMapping("/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.findById(id);
    }

    // 4. 本の検索: GET /api/v1/books/search?q={query}
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam("q") String query) {
        return bookService.search(query);
    }
}