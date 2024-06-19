package ru.rebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rebook.model.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
}
