package ru.rebook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rebook.exception.AuthorNotFoundException;
import ru.rebook.exception.BookNotFoundException;
import ru.rebook.model.converter.AuthorConverter;
import ru.rebook.model.converter.BookCollectionConverter;
import ru.rebook.model.converter.GenreConverter;
import ru.rebook.model.dto.BookByAuthorDto;
import ru.rebook.model.dto.BookFullInformationDto;
import ru.rebook.model.dto.PageDto;
import ru.rebook.model.entity.Book;
import ru.rebook.model.entity.Rating;
import ru.rebook.model.entity.User;
import ru.rebook.model.mapper.BookByAuthorDtoMapper;
import ru.rebook.model.mapper.BookFullInformationDtoMapper;
import ru.rebook.model.request.BookAddRequest;
import ru.rebook.model.request.RatingRequest;
import ru.rebook.repository.AuthorRepository;
import ru.rebook.repository.BookRepository;
import ru.rebook.repository.RatingRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final RatingRepository ratingRepository;

    private final AuthorConverter authorConverter;

    private final GenreConverter genreConverter;

    private final BookCollectionConverter bookCollectionConverter;

    private final BookByAuthorDtoMapper bookByAuthorDtoMapper;

    private final BookFullInformationDtoMapper bookFullInformationDtoMapper;

    @Transactional
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Long addBook(BookAddRequest request) {
        Book book = Book.builder()
                .title(request.title())
                .description(request.description())
                .authors(authorConverter.convertAuthorIdListToAuthors(request.authors()))
                .genres(genreConverter.convertGenreIdListToGenres(request.genres()))
                .collections(bookCollectionConverter.convertCollectionIdListToBookCollection(request.collections()))
                .build();
        bookRepository.save(book);

        return book.getId();
    }

    public PageDto<BookByAuthorDto> getBooksByAuthorId(Long authorId, int page, int size) {
        boolean isAuthorExists = authorRepository.existsById(authorId);
        if (!isAuthorExists) {
            throw new AuthorNotFoundException(authorId);
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by("rating").ascending());
        Page<Book> booksPage = bookRepository.findAllByAuthorId(pageable, authorId);
        List<BookByAuthorDto> books = booksPage.getContent().stream()
                .map(bookByAuthorDtoMapper)
                .toList();
        PageDto<BookByAuthorDto> response = new PageDto<>(
                books,
                booksPage.getTotalElements(),
                booksPage.getTotalPages()
        );

        return response;
    }

    public BookFullInformationDto getBookFullInformationDto(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new BookNotFoundException(bookId)
        );
        return bookFullInformationDtoMapper.apply(book);
    }

    @Transactional
    public void giveRatingToBook(RatingRequest request, User user) {
        Book book = bookRepository.findById(request.bookId()).orElseThrow(
                () -> new BookNotFoundException(request.bookId())
        );
        Rating rating = Rating.builder()
                .value(request.rating())
                .book(book)
                .user(user)
                .build();
        ratingRepository.save(rating);
        double overallRating = ratingRepository.averageByBookId(book.getId());
        book.setRating(overallRating);
        bookRepository.save(book);
    }
}
