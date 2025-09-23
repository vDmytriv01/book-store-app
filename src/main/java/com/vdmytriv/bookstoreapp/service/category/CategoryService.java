package com.vdmytriv.bookstoreapp.service.category;

import com.vdmytriv.bookstoreapp.dto.category.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    Page<CategoryDto> findAll(Pageable pageable);

    CategoryDto getById(Long id);

    CategoryDto save(CategoryDto dto);

    CategoryDto update(Long id, CategoryDto dto);

    void deleteById(Long id); // soft-delete
}
