package com.vdmytriv.bookstoreapp.service.category;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.vdmytriv.bookstoreapp.dto.category.CategoryDto;
import com.vdmytriv.bookstoreapp.dto.category.UpdateCategoryRequestDto;
import com.vdmytriv.bookstoreapp.mapper.CategoryMapper;
import com.vdmytriv.bookstoreapp.model.Category;
import com.vdmytriv.bookstoreapp.repository.category.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Given valid CategoryDto, "
            + "when save called, then category is saved and mapped to DTO")
    void save_ValidRequest_Success() {

        Category mapped = new Category();
        mapped.setName("Technology");
        mapped.setDescription("Books about IT and programming");

        Category saved = new Category();
        saved.setId(1L);
        saved.setName("Technology");
        saved.setDescription("Books about IT and programming");

        CategoryDto request = new CategoryDto(
                null, "Technology", "Books about IT and programming");

        CategoryDto expected = new CategoryDto(
                1L, "Technology", "Books about IT and programming");

        when(categoryMapper.toModel(request)).thenReturn(mapped);
        when(categoryRepository.save(mapped)).thenReturn(saved);
        when(categoryMapper.toDto(saved)).thenReturn(expected);

        CategoryDto actual = categoryService.save(request);

        assertEquals(expected, actual);
        verify(categoryMapper).toModel(request);
        verify(categoryRepository).save(mapped);
        verify(categoryMapper).toDto(saved);
    }

    @Test
    @DisplayName("Given existing category,"
            + " when update called, then fields are updated and DTO returned")
    void update_ExistingCategory_Success() {
        Long categoryId = 1L;

        Category existing = new Category();
        existing.setId(categoryId);
        existing.setName("Old Name");
        existing.setDescription("Old Description");
        UpdateCategoryRequestDto request = new UpdateCategoryRequestDto(
                "Updated Name", "Updated Description");

        Category updated = new Category();
        updated.setId(categoryId);
        updated.setName(request.name());
        updated.setDescription(request.description());

        CategoryDto expected = new CategoryDto(categoryId, request.name(), request.description());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existing));
        doAnswer(invocation -> {
            UpdateCategoryRequestDto dto = invocation.getArgument(0);
            Category c = invocation.getArgument(1);
            c.setName(dto.name());
            c.setDescription(dto.description());
            return null;
        }).when(categoryMapper).updateModel(request, existing);

        when(categoryRepository.save(existing)).thenReturn(updated);
        when(categoryMapper.toDto(updated)).thenReturn(expected);

        CategoryDto actual = categoryService.update(categoryId, request);

        assertEquals(expected, actual);
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).updateModel(request, existing);
        verify(categoryRepository).save(existing);
        verify(categoryMapper).toDto(updated);
    }

    @Test
    @DisplayName("Given non-existing category,"
            + " when update called, then throw EntityNotFoundException")
    void update_NonExistingCategory_ThrowsException() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
        UpdateCategoryRequestDto request =
                new UpdateCategoryRequestDto("Name", "Desc");

        assertThrows(EntityNotFoundException.class, () -> categoryService.update(99L, request));

        verify(categoryRepository).findById(99L);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }
}
