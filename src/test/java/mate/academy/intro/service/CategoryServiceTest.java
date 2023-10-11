package mate.academy.intro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import mate.academy.intro.dto.category.CategoryDto;
import mate.academy.intro.mapper.CategoryMapper;
import mate.academy.intro.model.Category;
import mate.academy.intro.repository.CategoryRepository;
import mate.academy.intro.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("Verify findAll() method works correctly")
    void findAll_validPageable_ReturnTwoCategories() {
        Pageable pageable = PageRequest.of(0, 10);
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Category 1");
        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setId(1L);
        categoryDto1.setName("Category 1");
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Category 2");
        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setId(2L);
        categoryDto2.setName("Category 2");
        Page<Category> categories = new PageImpl<>(List.of(category1, category2), pageable, 2L);
        when(categoryRepository.findAll(pageable)).thenReturn(categories);
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);

        List<CategoryDto> actual = categoryService.findAll(pageable);

        assertEquals(List.of(categoryDto1, categoryDto2), actual);
        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper,times(2)).toDto(any(Category.class));
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify getById() method works correctly")
    void getById_validId_ReturnCorrectCategoryDto() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Category 1");
        CategoryDto expected = new CategoryDto();
        expected.setId(1L);
        expected.setName("Category 1");
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(any(Category.class))).thenReturn(expected);

        CategoryDto actual = categoryService.getById(expected.getId());

        assertEquals(expected, actual);
        verify(categoryRepository, times(1)).findById(anyLong());
        verify(categoryMapper, times(1)).toDto(any(Category.class));
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify save() method works correctly")
    void save_validCategoryDto_ReturnCategoryDto() {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Category 1");
        Category category = new Category();
        category.setName("Category 1");
        category.setId(1L);
        CategoryDto expected = new CategoryDto();
        expected.setName("Category 1");
        expected.setId(1L);
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryMapper.toEntity(categoryDto)).thenReturn(category);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(expected);

        CategoryDto actual = categoryService.save(categoryDto);

        assertEquals(expected, actual);
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryMapper, times(1)).toEntity(categoryDto);
        verify(categoryMapper, times(1)).toDto(any(Category.class));
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }
}