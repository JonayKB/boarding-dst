package it.dst.garage.repository;

import java.util.List;

import it.dst.garage.model.entity.CarEntity;

public interface ICarRepository {
    public List<CarEntity> findAll();

    public CarEntity findById(String id);

    public boolean save(CarEntity car);

    public boolean update(CarEntity car);

    public boolean deleteById(String id);

    public boolean existsById(String id);

}
