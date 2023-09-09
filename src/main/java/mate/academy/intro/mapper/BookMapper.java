package mate.academy.intro.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.book.BookDto;
import mate.academy.intro.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.intro.dto.book.CreateBookRequestDto;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    Book updateBookFromDto(CreateBookRequestDto requestDto, @MappingTarget Book book);

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Category> categories = book.getCategories();
        List<Long> categoriesIds = new ArrayList<>(categories.size());
        for (Category category : categories) {
            categoriesIds.add(category.getId());
        }
        bookDto.setCategoryIds(categoriesIds);
    }
}
