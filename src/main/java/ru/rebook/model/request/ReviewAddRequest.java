package ru.rebook.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

@Schema(description = "Тело запроса на добавление отзыва к книге")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReviewAddRequest(

        @Schema(description = "Заголовок", example = "Онегин, добрый мой приятель")
        @NotBlank
        String title,

        @Schema(
                description = "Текст отзыва",
                example = """
                Евгений Онегин - это безусловно шедевр, который завораживает своей
                красотой и глубиной.
                """)
        @NotBlank
        String description,

        @Schema(
                description = "Идентификатор книги, к которой нужно оставить отзыв",
                example = "1"
        )
        @Positive
        Long bookId
) { }
