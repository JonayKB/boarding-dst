package it.dst.garage.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.dst.garage.dao.CarDao;
import it.dst.garage.dao.CarDaoFactory;
import it.dst.garage.exceptions.PersistanceTypeException;
import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.model.Car;

@Service
public class CarService {
    private CarDao carDao;
    @Autowired
    private CarDaoFactory carDaoFactory;

    public CarService(CarDaoFactory carDaoFactory) throws SQLException, PersistanceTypeException {
        this.carDaoFactory = carDaoFactory;

        this.carDao = this.carDaoFactory.create();
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
