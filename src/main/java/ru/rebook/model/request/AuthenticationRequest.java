package ru.rebook.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Тело запроса на аутентификацию пользователя")
public record AuthenticationRequest(

        @Schema(description = "Email", example = "myemail@gmail.com")
        @Email(message = "Email should be valid")
        String email,

        @Schema(description = "Пароль", example = "mypassword")
        @NotBlank
        String password
) { }
