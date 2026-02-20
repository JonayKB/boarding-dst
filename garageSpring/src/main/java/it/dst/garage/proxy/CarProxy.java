package it.dst.garage.proxy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.model.Car;
import it.dst.garage.service.CarService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CarProxy {
    @Autowired
    private CarService carService;

    private static final Logger LOG = LoggerFactory.getLogger(CarProxy.class);

    public List<Car> findAll() {
        List<Car> cars = carService.findAll();
        LOG.info("FindAll: Found " + cars.size() + " cars");
        return cars;
    }

    public Car findById(String id) {
        Car car = carService.findById(id);
        if (car == null) {
            LOG.info("FindById: Does not found car with id " + id);

        } else
            LOG.info("FindById: Found car with id " + car.getId());
        return car;
    }

    public boolean save(Car car) throws UnvalidCarException {
        boolean status = carService.save(car);
        if (status) {
            LOG.info("Save: Saved succefuly car with id " + car.getId());
        } else
            LOG.info("Save: Failed to save car with id " + car.getId());
        return status;
    }

    public boolean update(Car car) throws UnvalidCarException {
        boolean status = carService.update(car);
        if (status) {
            LOG.info("Update: Updated succefuly car with id " + car.getId());
        } else
            LOG.info("Update: Failed to update car with id " + car.getId());
        return status;
    }

    public boolean delete(String id) {
        boolean status = carService.delete(id);
        if (status) {
            LOG.info("Delete: Deleted succefuly car with id " + id);
        } else
            LOG.info("Delete: Failed to delete car with id " + id);
        return status;
    }

    public boolean existsById(String id) {
        boolean status = carService.existsById(id);
        if (status) {
            LOG.info("ExistById: Car with id " + id + " exists");
        } else
            LOG.info("ExistById: Car with id " + id + " does not exists");

        return status;
    }
}
