package ru.rebook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rebook.model.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("""
            SELECT r 
            FROM Review r 
            JOIN r.book b
            WHERE b.id = :bookId 
            """)
    Page<Review> findAllByBookId(Pageable pageable, @Param("bookId") Long bookId);
}
