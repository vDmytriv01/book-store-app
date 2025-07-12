package com.vdmytriv.bookstoreapp.mapper;

import com.vdmytriv.bookstoreapp.config.MapperConfig;
import com.vdmytriv.bookstoreapp.dto.BookDto;
import com.vdmytriv.bookstoreapp.dto.CreateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.PartialUpdateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.UpdateBookRequestDto;
import com.vdmytriv.bookstoreapp.model.Book;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    void updateModel(
            UpdateBookRequestDto updateBookRequestDto,
            @MappingTarget Book book);

    @AfterMapping
    default void partialUpdateModel(
            PartialUpdateBookRequestDto dto,
            @MappingTarget Book book) {
        dto.title().ifPresent(book::setTitle);
        dto.author().ifPresent(book::setAuthor);
        dto.isbn().ifPresent(book::setIsbn);
        dto.price().ifPresent(book::setPrice);
        dto.description().ifPresent(book::setDescription);
        dto.coverImage().ifPresent(book::setCoverImage);
    }
}

