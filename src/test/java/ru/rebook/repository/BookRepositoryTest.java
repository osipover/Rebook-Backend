package ru.rebook.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("/sql/prepare_db.sql")
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Test
    void countBooksByAuthorId_ExistentAuthor() {
        Long authorId = 1L;
        int numBooksByAuthor = bookRepository.countBooksByAuthorId(authorId);
        assertEquals(2, numBooksByAuthor);
    }

    @Test
    void countBooksByAuthorId_NonExistentAuthor() {
        Long authorId = 10L;
        int numBooksByAuthor = bookRepository.countBooksByAuthorId(authorId);
        assertEquals(0, numBooksByAuthor);
    }

    @Test
    void findAllByAuthorId_ExistentAuthor() {
        Long authorId = 1L;
        Pageable pageable = PageRequest.of(0, 1, Sort.by("rating").ascending());
        var page = bookRepository.findAllByAuthorId(pageable, authorId);
        assertEquals(2, page.getTotalElements());
        assertEquals(1, page.getContent().size());
    }

    @Test
    void findAllByAuthorId_NonExistentAuthor() {
        Long authorId = 10L;
        Pageable pageable = PageRequest.of(0, 1, Sort.by("rating").ascending());
        var page = bookRepository.findAllByAuthorId(pageable, authorId);
        assertEquals(0, page.getTotalElements());
        assertEquals(0, page.getContent().size());
    }
}