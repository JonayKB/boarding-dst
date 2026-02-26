package it.dst.garage.security.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.dst.garage.mapper.IUserDtoMapper;
import it.dst.garage.model.dto.UserDto;
import it.dst.garage.security.dto.LoginDto;
import it.dst.garage.security.dto.SignupDto;
import it.dst.garage.security.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.java.Log;

@RestController
@RequestMapping("/api/auth")
@Log
@Tag(name = "Authentication API", description = "Endpoints for user authentication and registration")
public class AuthController {
    private final AuthService authService;
    private final IUserDtoMapper userDtoMapper;

    public AuthController(AuthService authService, IUserDtoMapper userDtoMapper) {
        this.authService = authService;
        this.userDtoMapper = userDtoMapper;
    }

    @PostMapping("/signup/")
    @Operation(summary = "User registration", description = "Registers a new user with email, username and password")
    public UserDto signup(@Valid @RequestBody SignupDto signupDto) {
        return userDtoMapper.toDto(authService.signup(signupDto.email(), signupDto.username(), signupDto.password()));
    }

    @PostMapping("/login/")
    @Operation(summary = "User login", description = "Authenticates a user with email and password")
    public String login(@Valid @RequestBody LoginDto loginDto) {
        return authService.login(loginDto.email(), loginDto.password());
    }


}
