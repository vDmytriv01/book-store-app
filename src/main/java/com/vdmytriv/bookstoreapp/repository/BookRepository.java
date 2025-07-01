package com.vdmytriv.bookstoreapp.repository;

import com.vdmytriv.bookstoreapp.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
