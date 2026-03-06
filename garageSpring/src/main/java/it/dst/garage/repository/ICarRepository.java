package it.dst.garage.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import it.dst.garage.model.entity.CarEntity;

public interface ICarRepository {
    public List<CarEntity> findAll();

    public Page<CarEntity> findAll(Pageable pageable);

    public CarEntity findById(String id);

    public CarEntity save(CarEntity car);

    public CarEntity update(CarEntity car);

    public boolean deleteById(String id);

    public boolean existsById(String id);

    public Page<CarEntity> findByBrandContaining(String brand, Pageable pageable);

}
