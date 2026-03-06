package it.dst.garage.proxy;

import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.model.Car;
import it.dst.garage.service.CarService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CarProxy {

    private final CarService carService;

    public List<Car> findAll() {
        List<Car> cars = carService.findAll();
        log.info("FindAll: Found {} cars", cars.size());
        return cars;
    }

    public Page<Car> findAll(int page, int size) {
        Page<Car> cars = carService.findAll(page, size);
        log.info("FindAll with pagination: Found {} cars on page {}", cars.getNumberOfElements(), page);
        return cars;
    }

    public Car findById(String id) {
        Car car = carService.findById(id);
        if (car == null) {
            log.warn("FindById: Car with id {} not found", id);
        } else {
            log.info("FindById: Found car with id {}", car.getId());
        }
        return car;
    }

    public Page<Car> findByBrandContaining(String brand, int page, int size) {
        Page<Car> cars = carService.findByBrandContaining(brand, page, size);
        log.info("FindByBrandContaining: Found {} cars containing brand '{}'", cars.getNumberOfElements(), brand);
        return cars;
    }

    public boolean save(Car car) throws UnvalidCarException {
        Car savedCar = carService.save(car);
        if (savedCar != null) {
            log.info("Save: Successfully saved car with id {} by {} at {}", savedCar.getId(), savedCar.getCreatedBy(),
                    savedCar.getCreatedAt());
        } else {
            log.error("Save: Failed to save car with id {}", car.getId());
        }
        return savedCar != null;
    }

    public boolean update(Car car) throws UnvalidCarException {
        Car updatedCar = carService.update(car);
        if (updatedCar != null) {
            log.info("Update: Successfully updated car with id {} by {} at {}", updatedCar.getId(),
                    updatedCar.getModifiedBy(), updatedCar.getModifiedAt());
        } else {
            log.error("Update: Failed to update car with id {}", car.getId());
        }
        return updatedCar != null;
    }

    public boolean delete(String id) {
        boolean status = carService.delete(id);
        if (status) {
            log.info("Delete: Successfully deleted car with id {} by {}", id,
                    SecurityContextHolder.getContext().getAuthentication() == null ? "anonymous"
                            : SecurityContextHolder.getContext().getAuthentication().getName());
        } else {
            log.error("Delete: Failed to delete car with id {}", id);
        }
        return status;
    }

    public boolean existsById(String id) {
        boolean exists = carService.existsById(id);
        log.info("ExistsById: Car with id {} exists: {}", id, exists);
        return exists;
    }

}