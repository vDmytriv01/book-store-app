package com.vdmytriv.bookstoreapp.mapper;

import com.vdmytriv.bookstoreapp.config.MapperConfig;
import com.vdmytriv.bookstoreapp.dto.book.BookDto;
import com.vdmytriv.bookstoreapp.dto.book.CreateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.book.PartialUpdateBookRequestDto;
import com.vdmytriv.bookstoreapp.dto.book.UpdateBookRequestDto;
import com.vdmytriv.bookstoreapp.model.Book;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface BookMapper {

    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);

    void updateModel(
            UpdateBookRequestDto updateBookRequestDto,
            @MappingTarget Book book);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void partialUpdateModel(
            PartialUpdateBookRequestDto dto,
            @MappingTarget Book book);
}
