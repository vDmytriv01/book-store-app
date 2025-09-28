package com.vdmytriv.bookstoreapp.mapper;

import com.vdmytriv.bookstoreapp.config.MapperConfig;
import com.vdmytriv.bookstoreapp.dto.category.CategoryDto;
import com.vdmytriv.bookstoreapp.dto.category.UpdateCategoryRequestDto;
import com.vdmytriv.bookstoreapp.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toModel(CategoryDto dto);

    void updateModel(
            UpdateCategoryRequestDto requestDto,
            @MappingTarget Category category);
}
