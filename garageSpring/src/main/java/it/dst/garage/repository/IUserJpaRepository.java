package it.dst.garage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.dst.garage.model.entity.UserEntity;

public interface IUserJpaRepository extends JpaRepository<UserEntity, String> {
    UserEntity findByEmail(String email);

    boolean existsByEmail(String email);

}
