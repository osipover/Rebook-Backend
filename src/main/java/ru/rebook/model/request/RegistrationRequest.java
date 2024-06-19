package ru.rebook.model.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Тело запроса на регистрацию пользователя")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record RegistrationRequest(

        @Schema(description = "Email", example = "myemail@gmail.com")
        @Email(message = "Email should be valid")
        String email,

        @Schema(description = "Пароль", example = "mypassword")
        @NotBlank
        String password,

        @Schema(description = "Имя", example = "Александр")
        @NotBlank
        String firstName,

        @Schema(description = "Фамилия", example = "Осипов")
        @NotBlank
        String lastName
) { }
