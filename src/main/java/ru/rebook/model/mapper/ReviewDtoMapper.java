package ru.rebook.model.mapper;

import org.springframework.stereotype.Component;
import ru.rebook.model.dto.ReviewDto;
import ru.rebook.model.entity.Review;

import java.util.function.Function;

@Component
public class ReviewDtoMapper implements Function<Review, ReviewDto> {
    @Override
    public ReviewDto apply(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .title(review.getTitle())
                .description(review.getDescription())
                .dateOfCreation(review.getDateOfCreation())
                .userFirstName(review.getUser().getFirstName())
                .userLastName(review.getUser().getLastName())
                .build();
    }
}
