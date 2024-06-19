package ru.rebook.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(description = "Тело запроса на добавление новой книги в базу")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record BookAddRequest(

        @Schema(description = "Название книги", example = "Евгений Онегин")
        @NotBlank
        String title,

        @Schema(
                description = "Краткое описание книги",
                example = """
                Роман в стихах «Евгений Онегин» по праву можно считать энциклопедией русской жизни — в нем 
                отразились быт и нравы первой трети XIX века, и энциклопедией мировой культуры 
                """)
        @NotBlank
        String description,

        @Schema(
                description = "Идентификаторы авторов",
                example = "[1]"
        )
        List<Long> authors,

        @Schema(
                description = "Идентификаторы жанров",
                example = "[3]"
        )
        List<Long> genres,

        @Schema(
                description = "Идентификаторы подборок",
                example = "[4]"
        )
        List<Long> collections
) { }
