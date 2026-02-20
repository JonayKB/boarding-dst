package it.dst.garage.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.dst.garage.dao.CarDao;
import it.dst.garage.dao.CarDaoFactory;
import it.dst.garage.exceptions.PersistanceTypeException;
import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.model.Car;
import it.dst.garage.utils.ITransactionManager;

@Service
public class CarService {
    private CarDao carDao;
    @Autowired
    private CarDaoFactory carDaoFactory;

    @Autowired
    private ITransactionManager transactionManager;

    public CarService(CarDaoFactory carDaoFactory, ITransactionManager transactionManager)
            throws SQLException, PersistanceTypeException, IOException {
        this.carDaoFactory = carDaoFactory;
        this.transactionManager = transactionManager;

        this.carDao = this.carDaoFactory.create();
    }

    public List<Car> findAll() {
        return carDao.findAll();
    }

    public Car findById(String id) {
        return carDao.findById(id);
    }

    public boolean save(Car car) throws UnvalidCarException {
        try {
            return transactionManager.inTransaction(() -> {
                return carDao.save(car);
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Car car) {
        if (carDao.existsById(car.getId())) {
            try {
                return transactionManager.inTransaction(() -> {
                    return carDao.update(car);
                });
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean delete(String id) {
        try {
            return transactionManager.inTransaction(() -> {
                return carDao.delete(id);
            });
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsById(String id) {
        return carDao.existsById(id);
    }

}
