package com.vdmytriv.bookstoreapp.service.category;

import com.vdmytriv.bookstoreapp.dto.category.CategoryDto;
import com.vdmytriv.bookstoreapp.mapper.CategoryMapper;
import com.vdmytriv.bookstoreapp.model.Category;
import com.vdmytriv.bookstoreapp.repository.category.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Page<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(categoryMapper::toDto);
    }

    @Override
    public CategoryDto getById(Long id) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found id=" + id));
        return categoryMapper.toDto(c);
    }

    @Override
    public CategoryDto save(CategoryDto dto) {
        Category saved = categoryRepository.save(categoryMapper.toModel(dto));
        return categoryMapper.toDto(saved);
    }

    @Override
    public CategoryDto update(Long id, CategoryDto dto) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found id=" + id));
        c.setName(dto.name());
        c.setDescription(dto.description());
        return categoryMapper.toDto(categoryRepository.save(c));
    }

    @Override
    public void deleteById(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category not found id=" + id);
        }
        categoryRepository.deleteById(id);
    }
}
