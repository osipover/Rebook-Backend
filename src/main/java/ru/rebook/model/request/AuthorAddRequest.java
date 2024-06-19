package ru.rebook.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Тело запроса на добавление нового автора в базу")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record AuthorAddRequest(

        @Schema(description = "Имя автора", example = "Александр")
        @NotBlank
        String firstName,

        @Schema(description = "Фамилия автора", example = "Пушкин")
        @NotBlank
        String lastName,

        @Schema(description = "Отчество автора", example = "Сергеевич")
        String middleName,

        @Schema(description = "Дата рождения (dd.MM.yyyy)", example = "06.06.1799")
        @Pattern(regexp = "\\d{2}\\.\\d{2}\\.\\d{4}")
        String dateOfBirth
) { }
