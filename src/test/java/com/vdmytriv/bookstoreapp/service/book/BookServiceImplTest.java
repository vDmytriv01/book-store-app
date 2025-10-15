package com.vdmytriv.bookstoreapp.service.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.vdmytriv.bookstoreapp.dto.book.BookDto;
import com.vdmytriv.bookstoreapp.dto.book.CreateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.book.UpdateBookRequestDto;
import com.vdmytriv.bookstoreapp.exception.EntityNotFoundException;
import com.vdmytriv.bookstoreapp.mapper.BookMapper;
import com.vdmytriv.bookstoreapp.model.Book;
import com.vdmytriv.bookstoreapp.model.Category;
import com.vdmytriv.bookstoreapp.repository.book.BookRepository;
import com.vdmytriv.bookstoreapp.repository.category.CategoryRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Given valid CreateBookRequestDto, "
            + "when save called, then book is persisted and mapped to DTO")
    void save_ValidRequest_Success() {
        CreateBookRequestDto request = new CreateBookRequestDto(
                "Effective Java",
                "Joshua Bloch",
                "978-3-16-148410-0",
                BigDecimal.valueOf(49.99),
                "A book about Java best practices",
                "https://example.com/image.jpg",
                List.of(1L)
        );

        Book book = new Book();
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setIsbn(request.isbn());
        book.setPrice(request.price());

        Category category = new Category();
        category.setId(1L);
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle(request.title());
        savedBook.setAuthor(request.author());
        savedBook.setIsbn(request.isbn());
        savedBook.setPrice(request.price());
        savedBook.setCategories(Set.of(category));

        BookDto expectedDto = new BookDto(
                1L,
                request.title(),
                request.author(),
                request.isbn(),
                request.price(),
                request.description(),
                request.coverImage(),
                request.categoryIds()
        );

        when(bookMapper.toModel(request)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.toDto(savedBook)).thenReturn(expectedDto);

        BookDto actual = bookService.save(request);

        assertEquals(expectedDto, actual);
        verify(bookRepository).save(book);
        verify(categoryRepository).getReferenceById(1L);
    }

    @Test
    @DisplayName("Given existing book, "
            + "when update called, then fields are updated and DTO returned")
    void update_ExistingBook_Success() {
        Long bookId = 1L;
        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle("Old title");

        Category category = new Category();
        category.setId(1L);
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);

        UpdateBookRequestDto request = new UpdateBookRequestDto(
                "New title",
                "Joshua Bloch",
                "978-3-16-148410-0",
                BigDecimal.valueOf(59.99),
                "Updated description",
                "https://example.com/image.jpg",
                List.of(1L)
        );

        Book updatedBook = new Book();
        updatedBook.setId(bookId);
        updatedBook.setTitle(request.title());
        updatedBook.setAuthor(request.author());
        updatedBook.setIsbn(request.isbn());
        updatedBook.setPrice(request.price());
        updatedBook.setCategories(Set.of(category));

        BookDto expectedDto = new BookDto(
                bookId,
                request.title(),
                request.author(),
                request.isbn(),
                request.price(),
                request.description(),
                request.coverImage(),
                request.categoryIds()
        );

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        doAnswer(invocation -> {
            UpdateBookRequestDto dto = invocation.getArgument(0);
            Book b = invocation.getArgument(1);
            b.setTitle(dto.title());
            b.setAuthor(dto.author());
            b.setPrice(dto.price());
            return null;
        }).when(bookMapper).updateModel(request, existingBook);

        when(bookRepository.save(existingBook)).thenReturn(updatedBook);
        when(bookMapper.toDto(updatedBook)).thenReturn(expectedDto);

        BookDto actual = bookService.update(bookId, request);

        assertEquals(expectedDto, actual);
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(existingBook);
        verify(categoryRepository).getReferenceById(1L);
    }

    @Test
    @DisplayName("Given non-existing book ID, "
            + "when update called, then throw EntityNotFoundException")
    void update_NonExistingBook_ThrowsException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        UpdateBookRequestDto request = new UpdateBookRequestDto(
                "Title", "Author", "123", BigDecimal.ONE, "desc", "img", List.of(1L)
        );

        assertThrows(EntityNotFoundException.class, () -> bookService.update(99L, request));

        verify(bookRepository).findById(99L);
        verifyNoMoreInteractions(bookRepository, categoryRepository);
    }
}
