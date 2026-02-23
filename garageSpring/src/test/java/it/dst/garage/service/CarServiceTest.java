package it.dst.garage.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.dst.garage.exceptions.PersistanceTypeException;
import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.mapper.ICarEntityMapper;
import it.dst.garage.mapper.ICarEntityMapperImpl;
import it.dst.garage.model.entity.CarEntity;
import it.dst.garage.repository.ICarRepository;

public class CarServiceTest {

    private CarService carService;
    @Mock
    private ICarRepository carRepository;

    private ICarEntityMapper carEntityMapper = new ICarEntityMapperImpl();

    @BeforeEach
    protected void beforeEach() throws SQLException, PersistanceTypeException, IOException {
        MockitoAnnotations.openMocks(this);
        carService = new CarService(carRepository, carEntityMapper);
    }

    @Test
    protected void test_findAll_success() {
        List<CarEntity> cars = new ArrayList<>();
        cars.add(new CarEntity("ID", "BRAND", "MODEL", 10, "PLACE"));
        when(carRepository.findAll()).thenReturn(cars);
        assertEquals(carService.findAll().size(), 1);
    }

    @Test
    protected void test_findById_success() {
        CarEntity carEntity = new CarEntity("ID", "BRAND", "MODEL", 10, "PLACE");
        when(carRepository.findById(anyString())).thenReturn(carEntity);
        assertEquals(carService.findById(anyString()), carEntityMapper.toModel(carEntity));
    }

    @Test
    protected void test_save_success() throws UnvalidCarException, SQLException {
        CarEntity carEntity = new CarEntity("ID", "BRAND", "MODEL", 10, "PLACE");

        when(carRepository.save(any(CarEntity.class))).thenReturn(true);

        assertTrue(carService.save(carEntityMapper.toModel(carEntity)));
    }

    @Test
    protected void test_update_success() throws UnvalidCarException, SQLException {
        CarEntity carEntity = new CarEntity("ID", "BRAND", "MODEL", 10, "PLACE");

        when(carRepository.existsById(anyString())).thenReturn(true);
        when(carRepository.update(any(CarEntity.class))).thenReturn(true);
        assertTrue(carService.update(carEntityMapper.toModel(carEntity)));
    }

    @Test
    protected void test_update_not_exists() throws UnvalidCarException {
        CarEntity carEntity = new CarEntity("ID", "BRAND", "MODEL", 10, "PLACE");

        when(carRepository.existsById(anyString())).thenReturn(false);
        assertFalse(carService.update(carEntityMapper.toModel(carEntity)));
    }

    @Test
    protected void test_delete_success() throws SQLException {

        when(carRepository.deleteById(anyString())).thenReturn(true);

        assertTrue(carService.delete(anyString()));
    }

    @Test
    protected void test_existsById_success() {

        when(carRepository.existsById(anyString())).thenReturn(true);
        assertTrue(carService.existsById(anyString()));
    }
}
