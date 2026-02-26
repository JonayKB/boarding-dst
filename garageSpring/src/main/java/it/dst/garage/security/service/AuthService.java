package it.dst.garage.security.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.dst.garage.mapper.IUserEntityMapper;
import it.dst.garage.model.User;
import it.dst.garage.repository.IUserRepository;
import it.dst.garage.security.exceptions.UnvalidUserException;

@Service
public class AuthService {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(IUserRepository userRepository, IUserEntityMapper userEntityMapper,
            PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User signup(String email, String username, String password) {
        User user = new User(email, username, password);
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UnvalidUserException("User already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userEntityMapper.toModel(userRepository.save(userEntityMapper.toEntity(user)));
    }

    public String login(String email, String password) {
        User user = userEntityMapper.toModel(userRepository.findByEmail(email));
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new UnvalidUserException("User not found or invalid password");
        }
        return jwtService.generateToken(user);
    }

}
