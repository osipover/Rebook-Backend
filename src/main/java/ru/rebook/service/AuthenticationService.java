package ru.rebook.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.rebook.exception.FailedUserRegistrationException;
import ru.rebook.exception.NonExistentRoleException;
import ru.rebook.model.dto.AuthenticationDto;
import ru.rebook.model.entity.Role;
import ru.rebook.model.entity.User;
import ru.rebook.model.request.AuthenticationRequest;
import ru.rebook.model.request.RegistrationRequest;
import ru.rebook.repository.RoleRepository;
import ru.rebook.repository.UserRepository;
import ru.rebook.security.JwtService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationDto register(RegistrationRequest request) {
        boolean isEmailAlreadyTaken = userRepository.existsByEmail(request.email());
        if (isEmailAlreadyTaken) {
            throw new FailedUserRegistrationException(request.email());
        }
        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow(
                () -> new NonExistentRoleException("ROLE_USER")
        );
        User user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        String token = jwtService.generateToken(user);

        return AuthenticationDto.builder()
                .token(token)
                .build();
    }

    public AuthenticationDto authenticate(AuthenticationRequest request) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(),
                request.password()
        ));

        User user = (User) auth.getPrincipal();
        String token = jwtService.generateToken(user);

        return AuthenticationDto.builder()
                .token(token)
                .build();
    }
}
