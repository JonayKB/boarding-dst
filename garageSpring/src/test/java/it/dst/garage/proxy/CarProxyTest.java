package it.dst.garage.proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.model.Car;
import it.dst.garage.service.CarService;

public class CarProxyTest {
    private static final String TEST_PLATE = "TEST_PLATE";

    private static final int TEST_YEAR = 2000;

    private static final String TEST_MODEL = "TEST_MODEL";

    private static final String TEST_BRAND = "TEST_BRAND";

    private static final String TEST_ID = "TEST_ID";

    @Mock
    private CarService carService;

    private CarProxy carProxy;

    @BeforeEach
    protected void beforeEach() {
        MockitoAnnotations.openMocks(this);
        carProxy = new CarProxy(carService);
    }

    @Test
    protected void test_findAll_success() {
        List<Car> cars = new ArrayList<>();
        when(carService.findAll()).thenReturn(cars);
        assertEquals(cars, carProxy.findAll());
    }

    @Test
    protected void test_findById_success() {
        Car car = new Car();
        when(carService.findById(anyString())).thenReturn(car);
        assertEquals(car, carProxy.findById("ID"));
    }

    @Test
    protected void test_findById_error() {
        when(carService.findById(anyString())).thenReturn(null);
        assertNull(carProxy.findById("ID"));
    }

    @Test
    protected void test_save_success() throws UnvalidCarException {
        Car car = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        when(carService.save(any(Car.class))).thenReturn(car);
        assertTrue(carProxy.save(car));
    }

    @Test
    protected void test_save_error() throws UnvalidCarException {
        Car car = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        when(carService.save(any(Car.class))).thenReturn(null);
        assertFalse(carProxy.save(car));
    }

    @Test
    protected void test_update_success() throws UnvalidCarException {
        Car car = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        when(carService.update(any(Car.class))).thenReturn(car);
        assertTrue(carProxy.update(car));
    }

    @Test
    protected void test_update_error() throws UnvalidCarException {
        Car car = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        when(carService.update(any(Car.class))).thenReturn(null);
        assertFalse(carProxy.update(car));
    }

    @Test
    protected void test_existById_success() throws UnvalidCarException {
        when(carService.existsById(anyString())).thenReturn(true);
        assertTrue(carProxy.existsById(TEST_ID));
    }

    @Test
    protected void test_existById_error() throws UnvalidCarException {
        when(carService.existsById(anyString())).thenReturn(false);
        assertFalse(carProxy.existsById(TEST_ID));
    }

    @Test
    protected void test_delete_success() throws UnvalidCarException {
        when(carService.delete(anyString())).thenReturn(true);
        assertTrue(carProxy.delete(TEST_ID));
    }

    @Test
    protected void test_delete_error() throws UnvalidCarException {
        when(carService.delete(anyString())).thenReturn(false);
        assertFalse(carProxy.delete(TEST_ID));
    }
}
