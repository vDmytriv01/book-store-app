package com.vdmytriv.bookstoreapp.service.book;

import com.vdmytriv.bookstoreapp.dto.book.BookDto;
import com.vdmytriv.bookstoreapp.dto.book.BookSearchParametersDto;
import com.vdmytriv.bookstoreapp.dto.book.CreateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.book.PartialUpdateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.book.UpdateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto update(Long id, UpdateBookRequestDto requestDto);

    BookDto patch(Long id, PartialUpdateBookRequestDto requestDto);

    Page<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    void delete(Long id);

    Page<BookDto> search(BookSearchParametersDto params, Pageable pageable);
}
