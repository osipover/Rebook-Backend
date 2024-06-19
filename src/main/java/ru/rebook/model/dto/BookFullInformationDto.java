package ru.rebook.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookFullInformationDto {

    private Long id;

    private String title;

    private String description;

    private LocalDate dateOfPublication;

    private Double rating;

    private List<GenreDto> genres;

    private List<AuthorFullNameDto> authors;

    private List<BookCollectionDto> collections;

}
