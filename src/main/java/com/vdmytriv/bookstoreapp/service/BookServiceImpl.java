package com.vdmytriv.bookstoreapp.service;

import com.vdmytriv.bookstoreapp.dto.BookDto;
import com.vdmytriv.bookstoreapp.dto.BookSearchParametersDto;
import com.vdmytriv.bookstoreapp.dto.CreateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.PartialUpdateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.UpdateBookRequestDto;
import com.vdmytriv.bookstoreapp.exception.EntityNotFoundException;
import com.vdmytriv.bookstoreapp.mapper.BookMapper;
import com.vdmytriv.bookstoreapp.model.Book;
import com.vdmytriv.bookstoreapp.repository.book.BookRepository;
import com.vdmytriv.bookstoreapp.repository.book.BookSpecificationBuilder;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto update(Long id, UpdateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
        bookMapper.updateModel(requestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto patch(Long id, PartialUpdateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
        bookMapper.partialUpdateModel(requestDto, book);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found with id " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public void delete(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        }
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto params) {
        Specification<Book> spec = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(spec).stream()
                .map(bookMapper::toDto)
                .toList();
    }
}

