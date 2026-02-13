package it.dst.garage.dao;

import java.util.ArrayList;
import java.util.List;

import it.dst.garage.model.Car;

public class CarArrayDao implements CarDao {
    private List<Car> cars;

    public CarArrayDao() {
        this.cars = new ArrayList<>();
    }

    @Override
    public List<Car> findAll() {
        return cars;
    }

    @Override
    public Car findById(String id) {
        return cars.stream()
                .filter(car -> car.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean save(Car car) {
        return cars.add(car);
    }

    @Override
    public boolean update(Car car) {
        int index = cars.indexOf(car);
        if (index != -1) {
            cars.set(index, car);
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        return cars.removeIf(car -> car.getId().equals(id));
    }

    @Override
    public boolean existsById(String id) {
        return cars.stream().anyMatch(car -> car.getId().equals(id));
    }
}
