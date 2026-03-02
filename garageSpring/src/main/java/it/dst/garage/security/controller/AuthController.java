package it.dst.garage.security.controller;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
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
import jakarta.servlet.http.HttpServletResponse;
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

    @GetMapping("/user/")
    @Operation(summary = "Returns user data", description = "Returns user data based on the provided JWT token in the Authorization header")
    public String getUser(Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("No active user");
        }

        return userDtoMapper.toDto(authService.getUser(authentication)).toString();
    }

    @GetMapping("/login-google")
    @Operation(summary = "Login with Google", description = "### [CLICK here to login](http://localhost:8080/api/auth/login-google)\n\n"
            +
            "Do not use the 'Execute' button below, click on the link above.")
    public void redirectToGoogle(HttpServletResponse response) throws IOException {
        response.sendRedirect("/oauth2/authorization/google");
    }
}
