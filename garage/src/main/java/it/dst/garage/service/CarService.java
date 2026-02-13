package it.dst.garage.service;

import java.time.Year;
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
        checkCarData(car,false);
        return carDao.save(car);
    }

    public boolean update(Car car) throws UnvalidCarException {
        if (carDao.existsById(car.getId())) {
            checkCarData(car,true);
            return carDao.update(car);
        }
        return false;
    }

    public boolean delete(String id) {
        return carDao.delete(id);
    }

    private void checkCarData(Car car, boolean updating) throws UnvalidCarException {
        if (car.getPlate() == null || car.getPlate().isEmpty()) {
            throw new UnvalidCarException("The plate should be empty");
        }
        if (updating) {
            if (!carDao.existsById(car.getId())) {
                throw new UnvalidCarException("A car with the same id already exists");
            }
        } else {
            if (carDao.existsById(car.getId())) {
                throw new UnvalidCarException("A car with the same id already exists");
            }
        }

        if (car.getYear() > Year.now().getValue()) {
            throw new UnvalidCarException("Year date must be previous to actual year");
        }
    }

}
