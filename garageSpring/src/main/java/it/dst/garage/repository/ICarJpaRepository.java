package it.dst.garage.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import it.dst.garage.model.entity.CarEntity;

public interface ICarJpaRepository extends JpaRepository<CarEntity, String> {
    public Page<CarEntity> findByBrandContaining(String brand, Pageable pageable);

}
