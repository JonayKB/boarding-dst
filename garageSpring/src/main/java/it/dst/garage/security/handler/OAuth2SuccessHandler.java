package it.dst.garage.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import it.dst.garage.mapper.IUserEntityMapper;
import it.dst.garage.model.User;
import it.dst.garage.repository.IUserRepository;
import it.dst.garage.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;

@Component
@Log
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    public OAuth2SuccessHandler(JwtService jwtService, IUserRepository userRepository,
            IUserEntityMapper userEntityMapper) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        User user = userEntityMapper.toModel(userRepository.findByEmail(email));
        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setUsername(oAuth2User.getAttribute("name"));
            userRepository.save(userEntityMapper.toEntity(user));
        }

        String myAppToken = jwtService.generateToken(user);
        response.setContentType("application/json");
        response.getWriter().write("{\"token\": \"" + myAppToken + "\"}");
    }
}