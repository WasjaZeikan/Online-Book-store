package mate.academy.intro.mapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.book.BookDto;
import mate.academy.intro.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.intro.dto.book.CreateBookRequestDto;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.Category;
import mate.academy.intro.repository.CategoryRepository;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(config = MapperConfig.class)
public abstract class BookMapper {
    @Autowired
    private CategoryRepository categoryRepository;

    public abstract BookDto toDto(Book book);

    public abstract Book toModel(CreateBookRequestDto requestDto);

    public abstract Book updateBookFromDto(CreateBookRequestDto requestDto,
                                           @MappingTarget Book book);

    public abstract BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    protected void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Category> categories = book.getCategories();
        List<Long> categoriesIds = new ArrayList<>(categories.size());
        for (Category category : categories) {
            categoriesIds.add(category.getId());
        }
        bookDto.setCategoryIds(categoriesIds);
    }

    @AfterMapping
    protected void setCategories(CreateBookRequestDto requestDto, @MappingTarget Book book) {
        Set<Category> categories = new HashSet<>(categoryRepository
                .findAllById(requestDto.getCategoryIds()));
        book.setCategories(categories);
    }
}
