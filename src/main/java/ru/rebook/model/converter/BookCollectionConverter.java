package ru.rebook.model.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.rebook.exception.BookCollectionNotFoundException;
import ru.rebook.model.entity.BookCollection;
import ru.rebook.repository.BookCollectionRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookCollectionConverter {

    private final BookCollectionRepository bookCollectionRepository;

    public List<BookCollection> convertCollectionIdListToBookCollection(List<Long> collectionIdList) {
        return collectionIdList.stream()
                .map(id -> bookCollectionRepository.findById(id)
                        .orElseThrow(() -> new BookCollectionNotFoundException(id)))
                .toList();
    }
}
