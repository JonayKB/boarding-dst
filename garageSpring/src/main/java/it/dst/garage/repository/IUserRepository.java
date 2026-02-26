package it.dst.garage.repository;

import it.dst.garage.model.entity.UserEntity;

public interface IUserRepository {

    UserEntity findByEmail(String email);

    UserEntity save(UserEntity user);

    boolean existsByEmail(String email);

}
