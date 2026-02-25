package it.dst.garage.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.mapper.ICarEntityMapper;
import it.dst.garage.model.Car;
import it.dst.garage.repository.ICarRepository;
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

    public Page<Car> findAll(int page, int size) {
        return carEntityMapper
                .toModel(carRepository.findAll(PageRequest.of(page, size)));
    }

    public Page<Car> findByBrandContaining(String brand, int page, int size) {
        return carEntityMapper
                .toModel(carRepository.findByBrandContaining(brand, PageRequest.of(page, size)));
    }

    public Car findById(String id) {
        return carEntityMapper.toModel(carRepository.findById(id));
    }

    public boolean save(Car car) throws UnvalidCarException {
        if (carRepository.existsById(car.getId())) {
            throw new UnvalidCarException("Car with id " + car.getId() + " already exists");
        }
        return carRepository.save(carEntityMapper.toEntity(car));

    }

    public boolean update(Car car) throws UnvalidCarException {
        if (!carRepository.existsById(car.getId())) {
            throw new UnvalidCarException("Car with id " + car.getId() + " does not exist");
        }
        return carRepository.update(carEntityMapper.toEntity(car));
    }

    public boolean delete(String id) {
        return carRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        return carRepository.existsById(id);
    }

}
