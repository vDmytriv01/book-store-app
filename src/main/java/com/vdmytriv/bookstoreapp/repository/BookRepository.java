package com.vdmytriv.bookstoreapp.repository;

import com.vdmytriv.bookstoreapp.model.Book;
import java.util.List;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

}
