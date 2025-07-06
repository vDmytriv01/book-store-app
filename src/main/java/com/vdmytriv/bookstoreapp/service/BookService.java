package com.vdmytriv.bookstoreapp.service;

import com.vdmytriv.bookstoreapp.dto.BookDto;
import com.vdmytriv.bookstoreapp.dto.BookSearchParametersDto;
import com.vdmytriv.bookstoreapp.dto.CreateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.UpdateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    BookDto update(Long id, UpdateBookRequestDto requestDto);

    List<BookDto> findAll();

    BookDto findById(Long id);

    void delete(Long id);

    List<BookDto> search(BookSearchParametersDto params);
}
