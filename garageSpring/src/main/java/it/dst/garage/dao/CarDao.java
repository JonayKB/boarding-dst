package it.dst.garage.dao;

import java.util.List;

import it.dst.garage.model.Car;

public interface CarDao {
    public List<Car> findAll();
    public Car findById(String id);
    public boolean save(Car car);
    public boolean update(Car car);
    public boolean delete(String id);
    public boolean existsById(String id);

}
