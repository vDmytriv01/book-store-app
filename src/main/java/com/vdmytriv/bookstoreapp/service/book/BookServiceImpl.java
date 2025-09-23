package com.vdmytriv.bookstoreapp.service.book;

import com.vdmytriv.bookstoreapp.dto.book.BookDto;
import com.vdmytriv.bookstoreapp.dto.book.BookSearchParametersDto;
import com.vdmytriv.bookstoreapp.dto.book.CreateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.book.PartialUpdateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.book.UpdateBookRequestDto;
import com.vdmytriv.bookstoreapp.exception.EntityNotFoundException;
import com.vdmytriv.bookstoreapp.mapper.BookMapper;
import com.vdmytriv.bookstoreapp.model.Book;
import com.vdmytriv.bookstoreapp.model.Category;
import com.vdmytriv.bookstoreapp.repository.book.BookRepository;
import com.vdmytriv.bookstoreapp.repository.book.BookSpecificationBuilder;
import com.vdmytriv.bookstoreapp.repository.category.CategoryRepository;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        setCategoriesFromIds(book, requestDto.categoryIds());
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto update(Long id, UpdateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
        bookMapper.updateModel(requestDto, book);
        setCategoriesFromIds(book, requestDto.categoryIds());
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public BookDto patch(Long id, PartialUpdateBookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id " + id));
        bookMapper.partialUpdateModel(requestDto, book);
        if (requestDto.categoryIds() != null) {
            setCategoriesFromIds(book, requestDto.categoryIds());
        }
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).map(bookMapper::toDto);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public Page<BookDto> search(BookSearchParametersDto params, Pageable pageable) {
        Specification<Book> spec = bookSpecificationBuilder.build(params);
        return bookRepository.findAll(spec, pageable)
                .map(bookMapper::toDto);
    }

    private void setCategoriesFromIds(Book book, java.util.List<Long> ids) {
        if (ids == null) {
            return;
        }
        Set<Category> categories = ids.stream()
                .map(categoryRepository::getReferenceById)
                .collect(Collectors.toSet());
        book.setCategories(categories);
    }
}
