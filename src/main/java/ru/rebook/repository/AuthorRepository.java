package ru.rebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.rebook.model.entity.Author;

import java.time.LocalDateTime;


public interface AuthorRepository extends JpaRepository<Author, Long> {

}
