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
class ReviewRepositoryTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Test
    void findAllByBookId_ExistentBook() {
        Long bookId = 1L;
        Pageable pageable = PageRequest.of(0, 1, Sort.by("dateOfCreation").ascending());
        var page = reviewRepository.findAllByBookId(pageable, bookId);
        assertEquals(2, page.getTotalElements());
        assertEquals(1, page.getContent().size());
    }

    @Test
    void findAllByBookId_NonExistentBook() {
        Long bookId = 20L;
        Pageable pageable = PageRequest.of(0, 1, Sort.by("dateOfCreation").ascending());
        var page = reviewRepository.findAllByBookId(pageable, bookId);
        assertEquals(0, page.getTotalElements());
        assertEquals(0, page.getContent().size());
    }

}