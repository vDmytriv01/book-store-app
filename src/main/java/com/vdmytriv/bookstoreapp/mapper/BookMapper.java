package com.vdmytriv.bookstoreapp.mapper;

import com.vdmytriv.bookstoreapp.config.MapperConfig;
import com.vdmytriv.bookstoreapp.dto.book.BookDto;
import com.vdmytriv.bookstoreapp.dto.book.BookDtoWithoutCategoryIds;
import com.vdmytriv.bookstoreapp.dto.book.CreateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.book.UpdateBookRequestDto;
import com.vdmytriv.bookstoreapp.model.Book;
import com.vdmytriv.bookstoreapp.model.Category;
import java.util.List;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    void updateModel(
            UpdateBookRequestDto updateBookRequestDto,
            @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto.BookDtoBuilder dto, Book book) {
        if (book.getCategories() != null) {
            List<Long> ids = book.getCategories()
                    .stream()
                    .map(Category::getId)
                    .toList();
            dto.categoryIds(ids);
        }
    }

    @Named("bookFromId")
    default Book bookFromId(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
