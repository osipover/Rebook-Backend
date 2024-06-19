package ru.rebook.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

@Schema(description = "Тело запроса на высталвение рейтинга книге")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RatingRequest(

        @Schema(description = "Идентификатор книги", example = "1")
        @Positive
        Long bookId,

        @Schema(description = "Оценка", example = "8")
        @Min(value = 1, message = "Rating should not be less then 1")
        @Max(value = 10, message = "Rating should not be greater then 10")
        Double rating
) { }
