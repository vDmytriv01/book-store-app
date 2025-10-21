package com.vdmytriv.bookstoreapp.service.category;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.vdmytriv.bookstoreapp.dto.category.CategoryDto;
import com.vdmytriv.bookstoreapp.dto.category.UpdateCategoryRequestDto;
import com.vdmytriv.bookstoreapp.exception.EntityNotFoundException;
import com.vdmytriv.bookstoreapp.mapper.CategoryMapper;
import com.vdmytriv.bookstoreapp.model.Category;
import com.vdmytriv.bookstoreapp.repository.category.CategoryRepository;
import com.vdmytriv.bookstoreapp.util.TestDataFactory;
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
        CategoryDto request = new CategoryDto(null, "Programming", "Tech books");

        Category mapped = TestDataFactory.createCategory().toBuilder().id(null).build();
        Category saved = TestDataFactory.createCategory();
        CategoryDto expected = new CategoryDto(1L, "Programming", "Tech books");

        when(categoryMapper.toModel(request)).thenReturn(mapped);
        when(categoryRepository.save(mapped)).thenReturn(saved);
        when(categoryMapper.toDto(saved)).thenReturn(expected);

        CategoryDto actual = categoryService.save(request);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);

        verify(categoryMapper).toModel(request);
        verify(categoryRepository).save(mapped);
        verify(categoryMapper).toDto(saved);
    }

    @Test
    @DisplayName("Given existing category,"
            + " when update called, then fields are updated and DTO returned")
    void update_ExistingCategory_Success() {
        Long categoryId = 1L;
        Category existing = TestDataFactory.createCategory();

        UpdateCategoryRequestDto request =
                new UpdateCategoryRequestDto("Updated Name", "Updated Description");
        Category updatedCategory = existing.toBuilder()
                .name(request.name())
                .description(request.description())
                .build();

        CategoryDto expectedDto =
                new CategoryDto(categoryId, request.name(), request.description());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existing));
        doAnswer(invocation -> {
            UpdateCategoryRequestDto dto = invocation.getArgument(0);
            Category c = invocation.getArgument(1);
            c.setName(dto.name());
            c.setDescription(dto.description());
            return null;
        }).when(categoryMapper).updateModel(request, existing);

        when(categoryRepository.save(existing)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expectedDto);

        CategoryDto actual = categoryService.update(categoryId, request);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expectedDto);

        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).updateModel(request, existing);
        verify(categoryRepository).save(existing);
        verify(categoryMapper).toDto(updatedCategory);
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
