package com.vdmytriv.bookstoreapp.util;

import com.vdmytriv.bookstoreapp.dto.book.BookDto;
import com.vdmytriv.bookstoreapp.dto.book.CreateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.book.UpdateBookRequestDto;
import com.vdmytriv.bookstoreapp.model.Book;
import com.vdmytriv.bookstoreapp.model.Category;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public final class TestDataFactory {
    private static final String BASE_TITLE = "Effective Java";
    private static final String BASE_AUTHOR = "Joshua Bloch";
    private static final String BASE_ISBN = "978-3-16-148410-1";
    private static final BigDecimal BASE_PRICE = BigDecimal.valueOf(49.99);
    private static final String BASE_DESCRIPTION = "A book about Java best practices.";
    private static final String BASE_IMAGE = "https://example.com/image.jpg";
    private static final String BASE_CATEGORY_NAME = "Programming";
    private static final String BASE_CATEGORY_DESCRIPTION = "Tech books";
    private static final List<Long> BASE_CATEGORY_IDS = List.of(1L);

    private TestDataFactory() {
    }

    public static Book createBook() {
        return Book.builder()
                .id(1L)
                .title(BASE_TITLE)
                .author(BASE_AUTHOR)
                .isbn(BASE_ISBN)
                .price(BASE_PRICE)
                .description(BASE_DESCRIPTION)
                .coverImage(BASE_IMAGE)
                .build();
    }

    public static Book createBookWithCategory() {
        Category category = createCategory();
        return createBook().toBuilder()
                .categories(Set.of(category))
                .build();
    }

    public static BookDto createBookDto() {
        return BookDto.builder()
                .id(1L)
                .title(BASE_TITLE)
                .author(BASE_AUTHOR)
                .isbn(BASE_ISBN)
                .price(BASE_PRICE)
                .description(BASE_DESCRIPTION)
                .coverImage(BASE_IMAGE)
                .categoryIds(BASE_CATEGORY_IDS)
                .build();
    }

    public static CreateBookRequestDto createBookRequestDto() {
        return new CreateBookRequestDto(
                BASE_TITLE,
                BASE_AUTHOR,
                BASE_ISBN,
                BASE_PRICE,
                BASE_DESCRIPTION,
                BASE_IMAGE,
                BASE_CATEGORY_IDS
        );
    }

    public static UpdateBookRequestDto createUpdateBookRequest() {
        return new UpdateBookRequestDto(
                "Updated title",
                BASE_AUTHOR,
                BASE_ISBN,
                BigDecimal.valueOf(59.99),
                "Updated description",
                BASE_IMAGE,
                BASE_CATEGORY_IDS
        );
    }

    public static Category createCategory() {
        return Category.builder()
                .id(1L)
                .name(BASE_CATEGORY_NAME)
                .description(BASE_CATEGORY_DESCRIPTION)
                .build();
    }

    public static Category createUpdatedCategory() {
        return Category.builder()
                .id(1L)
                .name("Updated Name")
                .description("Updated Description")
                .build();
    }

    public static Category createEmptyCategory() {
        return Category.builder()
                .name(BASE_CATEGORY_NAME)
                .description(BASE_CATEGORY_DESCRIPTION)
                .build();
    }
}
