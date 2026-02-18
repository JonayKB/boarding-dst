package it.dst.garage.service;

import java.util.List;

import it.dst.garage.dao.CarDao;
import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.model.Car;

public class CarService {
    private CarDao carDao;

    public CarService(CarDao carDao) {
        this.carDao = carDao;
    }

    public List<Car> findAll() {
        return carDao.findAll();
    }

    public Car findById(String id) {
        return carDao.findById(id);
    }

    public boolean save(Car car) throws UnvalidCarException {
        return carDao.save(car);
    }

    public boolean update(Car car) throws UnvalidCarException {
        if (carDao.existsById(car.getId())) {
            return carDao.update(car);
        }
        return false;
    }

    public boolean delete(String id) {
        return carDao.delete(id);
    }

    public boolean existsById(String id) {
        return carDao.existsById(id);
    }

}
