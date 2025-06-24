package com.vdmytriv.bookstoreapp.repository;

import com.vdmytriv.bookstoreapp.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> findById(Long id);

}
