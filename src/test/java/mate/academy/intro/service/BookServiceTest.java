package mate.academy.intro.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.intro.dto.book.BookDto;
import mate.academy.intro.dto.book.CreateBookRequestDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.BookMapper;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.Category;
import mate.academy.intro.repository.BookRepository;
import mate.academy.intro.service.impl.BookServiceImpl;
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
class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private BookMapper bookMapper;

    @Test
    @DisplayName("Verify save() method with correct CreateDto")
    void save_CorrectCreateRequestBookDto_returnBookDto() {
        //given
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setAuthor("Test Author");
        createBookRequestDto.setIsbn("12345678");
        createBookRequestDto.setPrice(BigDecimal.valueOf(100));
        createBookRequestDto.setTitle("Test title");
        createBookRequestDto.setDescription("Test description");
        createBookRequestDto.setCoverImage("Cover Image");
        createBookRequestDto.setCategoryIds(List.of(1L));

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setIsbn("12345678");
        book.setPrice(BigDecimal.valueOf(100));
        book.setTitle("Test title");
        book.setDescription("Test description");
        book.setCoverImage("Cover Image");
        book.setId(1L);

        Category category1 = new Category();
        category1.setName("category1");
        category1.setId(1L);
        book.setCategories(Set.of(category1));

        BookDto bookDto = new BookDto();
        bookDto.setAuthor("Test Author");
        bookDto.setIsbn("12345678");
        bookDto.setPrice(BigDecimal.valueOf(100));
        bookDto.setTitle("Test title");
        bookDto.setDescription("Test description");
        bookDto.setCoverImage("Cover Image");
        bookDto.setCategoryIds(List.of(1L));
        bookDto.setId(1L);

        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);

        //when
        BookDto actual = bookService.save(createBookRequestDto);

        //then
        assertEquals(bookDto, actual);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toDto(book);
        verify(bookMapper, times(1)).toModel(createBookRequestDto);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify findAll() method works with valid pageable")
    void findAll_validPageable_ReturnAllBooks() {
        //given
        Book book = new Book();
        book.setAuthor("Test Author");
        book.setIsbn("12345678");
        book.setPrice(BigDecimal.valueOf(100));
        book.setTitle("Test title");
        book.setDescription("Test description");
        book.setCoverImage("Cover Image");
        book.setId(1L);

        Category category1 = new Category();
        category1.setName("category1");
        category1.setId(1L);
        book.setCategories(Set.of(category1));

        BookDto bookDto = new BookDto();
        bookDto.setAuthor("Test Author");
        bookDto.setIsbn("12345678");
        bookDto.setPrice(BigDecimal.valueOf(100));
        bookDto.setTitle("Test title");
        bookDto.setDescription("Test description");
        bookDto.setCoverImage("Cover Image");
        bookDto.setCategoryIds(List.of(1L));
        bookDto.setId(1L);

        List<Book> books = List.of(book);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(books, pageable, books.size());

        when(bookMapper.toDto(book)).thenReturn(bookDto);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        //when
        List<BookDto> bookDtos = bookService.findAll(pageable);

        //then
        assertEquals(1, bookDtos.size());
        assertEquals(bookDto, bookDtos.get(0));
        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify getById() method works with valid id")
    void getById_validId_ReturnValidBookDto() {
        //given
        Book book = new Book();
        book.setAuthor("Test Author");
        book.setIsbn("12345678");
        book.setPrice(BigDecimal.valueOf(100));
        book.setTitle("Test title");
        book.setDescription("Test description");
        book.setCoverImage("Cover Image");
        book.setId(1L);

        Category category1 = new Category();
        category1.setName("category1");
        category1.setId(1L);
        book.setCategories(Set.of(category1));

        BookDto bookDto = new BookDto();
        bookDto.setAuthor("Test Author");
        bookDto.setIsbn("12345678");
        bookDto.setPrice(BigDecimal.valueOf(100));
        bookDto.setTitle("Test title");
        bookDto.setDescription("Test description");
        bookDto.setCoverImage("Cover Image");
        bookDto.setCategoryIds(List.of(1L));
        bookDto.setId(1L);

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        //when
        BookDto actual = bookService.getById(1L);

        //then

        assertEquals(bookDto, actual);
        verify(bookRepository, times(1)).findById(1L);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("Verify getById() method throw EntityNotFoundException")
    void getById_NotValidId_ThrowException() {
        //Given
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        //Then
        assertThrows(EntityNotFoundException.class,
                () -> bookService.getById(2L), "Can't get book by id: 2");
    }
}