package it.dst.garage.repository;

import org.springframework.stereotype.Repository;

import it.dst.garage.model.entity.UserEntity;

@Repository
public class UserJpaRepositoryImpl implements IUserRepository {

    private final IUserJpaRepository userJpaRepository;

    public UserJpaRepositoryImpl(IUserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public UserEntity save(UserEntity user) {
        return userJpaRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

}
