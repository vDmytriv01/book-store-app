package com.vdmytriv.bookstoreapp.service;

import com.vdmytriv.bookstoreapp.model.Book;
import java.util.List;

public interface BookService {
    Book save(Book book);

    List<Book> getBooks();
}
