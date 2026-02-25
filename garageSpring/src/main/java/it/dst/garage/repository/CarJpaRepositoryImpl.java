package it.dst.garage.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import it.dst.garage.model.entity.CarEntity;

@Repository
public class CarJpaRepositoryImpl implements ICarRepository {

    private final ICarJpaRepository carJpaRepository;

    public CarJpaRepositoryImpl(ICarJpaRepository carJpaRepository) {
        this.carJpaRepository = carJpaRepository;
    }

    @Override
    public List<CarEntity> findAll() {
        return carJpaRepository.findAll();
    }

    @Override
    public CarEntity findById(String id) {
        return carJpaRepository.findById(id).orElse(null);
    }

    @Override
    public boolean save(CarEntity car) {
        return carJpaRepository.save(car) != null;
    }

    @Override
    public boolean update(CarEntity car) {
        if (!carJpaRepository.existsById(car.getId())) {
            return false;
        }
        return carJpaRepository.save(car) != null;
    }

    @Override
    public boolean deleteById(String id) {
        if (!carJpaRepository.existsById(id)) {
            return false;
        }
        carJpaRepository.deleteById(id);
        return true;
    }

    @Override
    public boolean existsById(String id) {
        return carJpaRepository.existsById(id);
    }

    @Override
    public Page<CarEntity> findAll(Pageable pageable) {
        return carJpaRepository.findAll(pageable);
    }

    @Override
    public Page<CarEntity> findByBrandContaining(String brand, Pageable pageable) {
        return carJpaRepository.findByBrandContaining(brand, pageable);
    }

}
