package ru.rebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rebook.model.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    @Query("""
            SELECT AVG(r.value) FROM Rating r
            WHERE r.book.id = :bookId
            """)
    Double averageByBookId(@Param("bookId") Long bookId);
}
