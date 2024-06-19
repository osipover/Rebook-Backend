package ru.rebook.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rebook.model.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT COUNT(b) FROM Book b JOIN b.authors a WHERE a.id = :authorId")
    int countBooksByAuthorId(@Param("authorId") Long authorId);

    @Query("""
            SELECT b 
            FROM Book b 
            JOIN FETCH b.authors a 
            WHERE a.id = :authorId 
            """)
    Page<Book> findAllByAuthorId(Pageable pageable, @Param("authorId") Long authorId);
}
