package it.dst.garage.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import it.dst.garage.mapper.IUserEntityMapper;
import it.dst.garage.model.entity.UserEntity;
import it.dst.garage.repository.IUserRepository;
import lombok.extern.java.Log;

@Service
@Log
public class CustomUserDetailsService implements UserDetailsService {
    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    public CustomUserDetailsService(IUserRepository userRepository, IUserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity entity = userRepository.findByEmail(email);

        if (entity == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        return userEntityMapper.toModel(entity);
    }
}
