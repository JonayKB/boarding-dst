package it.dst.garage.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.mapper.ICarEntityMapper;
import it.dst.garage.model.Car;
import it.dst.garage.model.entity.CarEntity;
import it.dst.garage.repository.ICarRepository;
import it.dst.garage.specification.CarSpecifications;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CarService {

    private ICarRepository carRepository;

    private ICarEntityMapper carEntityMapper;

    public CarService(ICarRepository carRepository, ICarEntityMapper carEntityMapper) {
        this.carRepository = carRepository;
        this.carEntityMapper = carEntityMapper;
    }

    public List<Car> findAll() {
        return carEntityMapper.toModel(carRepository.findAll());
    }

    public Page<Car> findAll(int page, int size, Map<String, Object> filters, Sort sortOptions) {
        Specification<CarEntity> spec = Specification.unrestricted();
        if (filters.get("brand") != null) {
            spec = spec.and(CarSpecifications.hasBrand((String) filters.get("brand")));
        }
        if (filters.get("yearFrom") != null && filters.get("yearTo") != null) {
            spec = spec.and(CarSpecifications.betweenYears((Integer) filters.get("yearFrom"),
                    (Integer) filters.get("yearTo")));
        } else {
            if (filters.get("yearFrom") != null) {
                spec = spec.and(CarSpecifications.yearGreaterThan((Integer) filters.get("yearFrom")));
            }
            if (filters.get("yearTo") != null) {
                spec = spec.and(CarSpecifications.yearLowerThan((Integer) filters.get("yearTo")));
            }
        }
        if (filters.get("model") != null) {
            spec = spec.and(CarSpecifications.hasModel((String) filters.get("model")));
        }
        if (filters.get("plate") != null) {
            spec = spec.and(CarSpecifications.hasPlate((String) filters.get("plate")));
        }

        return carEntityMapper
                .toModel(carRepository.findAll(PageRequest.of(page, size).withSort(sortOptions), spec));
    }

    public Page<Car> findByBrandContaining(String brand, int page, int size) {
        return carEntityMapper
                .toModel(carRepository.findByBrandContaining(brand, PageRequest.of(page, size)));
    }

    public Car findById(String id) {
        return carEntityMapper.toModel(carRepository.findById(id));
    }

    public Car save(Car car) throws UnvalidCarException {
        if (carRepository.existsById(car.getId())) {
            throw new UnvalidCarException("Car with id " + car.getId() + " already exists");
        }
        return carEntityMapper.toModel(carRepository.save(carEntityMapper.toEntity(car)));

    }

    public Car update(Car car) throws UnvalidCarException {
        if (!carRepository.existsById(car.getId())) {
            throw new UnvalidCarException("Car with id " + car.getId() + " does not exist");
        }
        return carEntityMapper.toModel(carRepository.update(carEntityMapper.toEntity(car)));
    }

    public boolean delete(String id) {
        return carRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        return carRepository.existsById(id);
    }

}
