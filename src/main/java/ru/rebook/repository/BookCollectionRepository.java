package ru.rebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rebook.model.entity.BookCollection;

public interface BookCollectionRepository extends JpaRepository<BookCollection, Long> {

}
