package ru.rebook.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthorFullInformationDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String middleName;

    private LocalDate dateOfBirth;

    private int numWrittenBooks;
}
