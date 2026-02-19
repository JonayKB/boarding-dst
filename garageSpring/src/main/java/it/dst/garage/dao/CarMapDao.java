package it.dst.garage.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import it.dst.garage.model.Car;

@Repository
public class CarMapDao implements CarDao {
    Map<String, Car> cars;

    public CarMapDao() {
        cars = new HashMap<String, Car>();
    }

    @Override
    public List<Car> findAll() {
        return cars.values().stream().toList();
    }

    @Override
    public Car findById(String id) {
        return cars.get(id);
    }

    @Override
    public boolean save(Car car) {
        if (!existsById(car.getId())) {
            cars.put(car.getId(), car);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Car car) {
        if (existsById(car.getId())) {
            cars.replace(car.getId(), car);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        return cars.remove(id) != null;
    }

    @Override
    public boolean existsById(String id) {
        return cars.containsKey(id);
    }

    protected Map<String, Car> getCars() {
        return cars;
    }
}