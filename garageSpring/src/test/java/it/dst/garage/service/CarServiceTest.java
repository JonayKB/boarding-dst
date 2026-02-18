package it.dst.garage.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.dst.garage.dao.CarDao;
import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.model.Car;

public class CarServiceTest {

    private CarService carService;
    @Mock
    private CarDao carDaoMock;

    @BeforeEach
    protected void beforeEach() {
        MockitoAnnotations.openMocks(this);
        carService = new CarService(carDaoMock);

    }

    @Test
    protected void test_findAll_success() {
        List<Car> cars = new ArrayList<>();
        cars.add(new Car());
        when(carDaoMock.findAll()).thenReturn(cars);
        assertEquals(carService.findAll().size(), 1);
    }

    @Test
    protected void test_findById_success() {
        Car car = new Car("ID", "BRAND", "MODEL", 10, "PLACE");

        when(carDaoMock.findById(anyString())).thenReturn(car);
        assertEquals(carService.findById(anyString()), car);
    }

    @Test
    protected void test_save_success() throws UnvalidCarException {
        Car car = new Car("ID", "BRAND", "MODEL", 10, "PLACE");

        when(carDaoMock.save(any(Car.class))).thenReturn(true);
        assertTrue(carService.save(car));
    }

    @Test
    protected void test_update_success() throws UnvalidCarException {
        Car car = new Car("ID", "BRAND", "MODEL", 10, "PLACE");

        when(carDaoMock.existsById(anyString())).thenReturn(true);
        when(carDaoMock.update(any(Car.class))).thenReturn(true);
        assertTrue(carService.update(car));
    }

    @Test
    protected void test_update_not_exists() throws UnvalidCarException {
        Car car = new Car("ID", "BRAND", "MODEL", 10, "PLACE");

        when(carDaoMock.existsById(anyString())).thenReturn(false);
        assertFalse(carService.update(car));
    }

    @Test
    protected void test_delete_success() {

        when(carDaoMock.delete(anyString())).thenReturn(true);
        assertTrue(carService.delete(anyString()));
    }

    @Test
    protected void test_existsById_success() {

        when(carDaoMock.existsById(anyString())).thenReturn(true);
        assertTrue(carService.existsById(anyString()));
    }
}
