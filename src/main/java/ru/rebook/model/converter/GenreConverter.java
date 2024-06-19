package ru.rebook.model.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rebook.exception.GenreNotFoundException;
import ru.rebook.model.entity.Genre;
import ru.rebook.repository.GenreRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenreConverter {

    private final GenreRepository genreRepository;

    public List<Genre> convertGenreIdListToGenres(List<Long> genreIdList) {
        return genreIdList.stream()
                .map(id -> genreRepository.findById(id).orElseThrow(
                        () -> new GenreNotFoundException(id)))
                .toList();
    }
}
