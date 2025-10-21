package com.vdmytriv.bookstoreapp.service.book;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
import com.vdmytriv.bookstoreapp.util.TestDataFactory;
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
        CreateBookRequestDto request = TestDataFactory.createBookRequestDto();

        Book book = TestDataFactory.createBook();

        Category category = new Category();
        category.setId(1L);
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);

        Book savedBook = TestDataFactory.createBook()
                .toBuilder()
                .categories(Set.of(category))
                .build();

        BookDto expectedDto = TestDataFactory.createBookDto();

        when(bookMapper.toModel(request)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.toDto(savedBook)).thenReturn(expectedDto);

        BookDto actual = bookService.save(request);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expectedDto);

        verify(bookRepository).save(book);
        verify(categoryRepository).getReferenceById(1L);
    }

    @Test
    @DisplayName("Given existing book, "
            + "when update called, then fields are updated and DTO returned")
    void update_ExistingBook_Success() {
        Long bookId = 1L;
        Book existingBook = TestDataFactory.createBook();

        Category category = TestDataFactory.createCategory();
        when(categoryRepository.getReferenceById(1L)).thenReturn(category);

        UpdateBookRequestDto request = TestDataFactory.createUpdateBookRequest();

        Book updatedBook = TestDataFactory.createBook();

        BookDto expectedDto = TestDataFactory.createBookDto();

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

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expectedDto);

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
