package it.dst.garage.service;

import java.util.List;

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

    public Car findById(String id) {
        return carEntityMapper.toModel(carRepository.findById(id));
    }

    public boolean save(Car car)  {
        return carRepository.save(carEntityMapper.toEntity(car));

    }

    public boolean update(Car car) {
        return carRepository.update(carEntityMapper.toEntity(car));
    }

    public boolean delete(String id) {
        return carRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        return carRepository.existsById(id);
    }

}
