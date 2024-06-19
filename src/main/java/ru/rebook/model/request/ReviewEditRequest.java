package ru.rebook.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Тело запроса на добавление отзыва к книге")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ReviewEditRequest(

        @Schema(description = "Заголовок", example = "Онегин, добрый мой")
        @NotBlank
        String title,

        @Schema(
                description = "Текст отзыва",
                example = """
                Евгений Онегин - это безусловно шедевр, который завораживает своей красотой 
                и глубиной. Рекомендую каждому прочитать!
                """)
        @NotBlank
        String description
) { }
