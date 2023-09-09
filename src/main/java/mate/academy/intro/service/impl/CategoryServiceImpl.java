package mate.academy.intro.service.impl;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.category.CategoryDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.CategoryMapper;
import mate.academy.intro.model.Category;
import mate.academy.intro.repository.CategoryRepository;
import mate.academy.intro.service.CategoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper.toDto(categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category with id: " + id)));
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        Category savedCategory = categoryRepository.save(categoryMapper.toEntity(categoryDto));
        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        Category categoryFromDb = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category with id: " + id));
        categoryFromDb.setName(categoryDto.getName());
        categoryFromDb.setDescription(categoryDto.getDescription());
        return categoryMapper.toDto(categoryRepository.save(categoryFromDb));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
