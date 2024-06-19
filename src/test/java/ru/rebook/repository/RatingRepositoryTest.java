package ru.rebook.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("/sql/prepare_db.sql")
class RatingRepositoryTest {

    @Autowired
    RatingRepository ratingRepository;

    @Test
    void averageByBookId_ExistentBook_ReturnsTotalRating() {
        Long bookId = 1L;
        double totalRating = ratingRepository.averageByBookId(bookId);
        assertEquals(8.0, totalRating);
    }

    @Test
    void averageByBookId_NonExistentBook_ReturnsTotalRating() {
        Long bookId = 10L;
        Double totalRating = ratingRepository.averageByBookId(bookId);
        assertNull(totalRating);
    }

}