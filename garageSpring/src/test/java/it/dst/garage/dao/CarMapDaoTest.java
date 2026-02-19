package it.dst.garage.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.dst.garage.model.Car;

public class CarMapDaoTest {
    private static final String TEST_PLATE = "TEST_PLATE";
    private static final int TEST_YEAR = 2020;
    private static final String TEST_MODEL = "TEST_MODEL";
    private static final String TEST_BRAND = "TEST_BRAND";
    private static final String TEST_ID = "TEST_ID";
    private CarMapDao carMapDao;

    @BeforeEach
    public void beforeEach() {
        carMapDao = new CarMapDao();
    }

    @Test
    protected void test_findAll_is_empty() {
        assertEquals(carMapDao.findAll(), new ArrayList<Car>(), "Initial list should be empty");
    }

    @Test
    protected void test_findById_is_null() {
        assertEquals(carMapDao.findById(TEST_ID), null, "Should return because that item is not in db");
    }

    @Test
    protected void test_delete_not_exists() {
        assertFalse(carMapDao.delete(TEST_ID), "Should return false");
    }

    @Test
    protected void test_existById_not_exists() {
        assertFalse(carMapDao.existsById(TEST_ID), "Should return false");
    }

    @Test
    protected void test_findById_success() {
        Map<String, Car> cars = carMapDao.getCars();
        cars.put(TEST_ID, new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE));

        Car car = carMapDao.findById(TEST_ID);

        assertNotNull(car);
        assertEquals(car.getId(), TEST_ID);
        assertEquals(car.getBrand(), TEST_BRAND);
        assertEquals(car.getModel(), TEST_MODEL);
        assertEquals(car.getPlate(), TEST_PLATE);
        assertEquals(car.getYear(), TEST_YEAR);
    }

    @Test
    protected void test_update_success() {
        Map<String, Car> cars = carMapDao.getCars();
        cars.put(TEST_ID, new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE));
        Car carUpdate = new Car(TEST_ID, "BRAND_UPDATE", "MODEL_UPDATE", 2023, "PLATE_UPDATE");
        assertTrue(carMapDao.update(carUpdate));
        Car carUpdateResult = cars.get(TEST_ID);
        assertEquals(carUpdate, carUpdateResult);
        assertEquals(carUpdate.getId(), carUpdateResult.getId());
        assertEquals(carUpdate.getBrand(), carUpdateResult.getBrand());
        assertEquals(carUpdate.getModel(), carUpdateResult.getModel());
        assertEquals(carUpdate.getPlate(), carUpdateResult.getPlate());
        assertEquals(carUpdate.getYear(), carUpdateResult.getYear());

    }

    @Test
    protected void test_update_not_exists() {
        assertFalse(carMapDao.update(new Car()));
    }

    @Test
    protected void test_save_success() {
        Map<String, Car> cars = carMapDao.getCars();
        Car carSave = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        assertTrue(carMapDao.save(carSave));
        Car carSaveResult = cars.get(TEST_ID);
        assertEquals(carSave, carSaveResult);
        assertEquals(carSave.getId(), carSaveResult.getId());
        assertEquals(carSave.getBrand(), carSaveResult.getBrand());
        assertEquals(carSave.getModel(), carSaveResult.getModel());
        assertEquals(carSave.getPlate(), carSaveResult.getPlate());
        assertEquals(carSave.getYear(), carSaveResult.getYear());
    }

    @Test
    protected void test_delete_success() {
        Map<String, Car> cars = carMapDao.getCars();
        Car carDelete = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        cars.put(TEST_ID, carDelete);
        assertTrue(carMapDao.delete(carDelete.getId()));
        assertEquals(cars.size(), 0);
    }

    @Test
    protected void test_existById_success() {
        Map<String, Car> cars = carMapDao.getCars();
        Car carExistsById = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        cars.put(TEST_ID, carExistsById);
        assertTrue(carMapDao.existsById(carExistsById.getId()));
    }

    @Test
    protected void test_save_error_exists() {
        Map<String, Car> cars = carMapDao.getCars();
        Car carSave = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        cars.put(TEST_ID, carSave);
        assertFalse(carMapDao.save(carSave));
    }

}
