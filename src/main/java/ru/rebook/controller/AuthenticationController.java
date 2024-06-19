package ru.rebook.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rebook.model.dto.AuthenticationDto;
import ru.rebook.model.request.AuthenticationRequest;
import ru.rebook.model.request.RegistrationRequest;
import ru.rebook.service.AuthenticationService;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "authentication", description = "API для регистрации и аутентификации с использованием JWT токена")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет зарегистрировать обычного пользователя"
    )
    @PostMapping("register")
    public ResponseEntity<AuthenticationDto> register(@RequestBody @Valid RegistrationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Позволяет аутентифицировать пользователя по почте и паролю"
    )
    @PostMapping("authenticate")
    public ResponseEntity<AuthenticationDto> authenticate(@RequestBody @Valid AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }
}
