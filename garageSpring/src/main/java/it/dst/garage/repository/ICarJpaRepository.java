package it.dst.garage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import it.dst.garage.model.entity.CarEntity;


public interface ICarJpaRepository extends JpaRepository<CarEntity, String> {
    
}
