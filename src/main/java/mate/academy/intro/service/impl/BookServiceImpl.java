package mate.academy.intro.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.book.BookDto;
import mate.academy.intro.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.intro.dto.book.CreateBookRequestDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.BookMapper;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.Category;
import mate.academy.intro.repository.BookRepository;
import mate.academy.intro.repository.CategoryRepository;
import mate.academy.intro.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        book.setCategories(getCategoriesFromIds(requestDto.getCategoryIds()));
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable).stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto getById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't get book by id: " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto updateById(Long id, CreateBookRequestDto requestDto) {
        Book bookFromDB = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't get book by id: " + id));
        bookFromDB.setCategories(getCategoriesFromIds(requestDto.getCategoryIds()));
        Book updatedBook = bookRepository.save(bookMapper
                .updateBookFromDto(requestDto, bookFromDB));
        return bookMapper.toDto(updatedBook);
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDtoWithoutCategoryIds> findAllByCategoryId(Pageable pageable, Long categoryId) {
        return bookRepository.findAllByCategoryId(pageable, categoryId).stream()
                .map(bookMapper::toDtoWithoutCategories)
                .toList();
    }

    private Set<Category> getCategoriesFromIds(List<Long> ids) {
        return new HashSet<>(categoryRepository.findAllById(ids));
    }
}
