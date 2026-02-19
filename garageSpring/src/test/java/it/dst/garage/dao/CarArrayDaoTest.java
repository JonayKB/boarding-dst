package it.dst.garage.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.dst.garage.model.Car;

public class CarArrayDaoTest {
    private static final String TEST_PLATE = "TEST_PLATE";
    private static final int TEST_YEAR = 2020;
    private static final String TEST_MODEL = "TEST_MODEL";
    private static final String TEST_BRAND = "TEST_BRAND";
    private static final String TEST_ID = "TEST_ID";
    private CarArrayDao carArrayDao;

    @BeforeEach
    public void beforeEach() {
        carArrayDao = new CarArrayDao();
    }

    @Test
    protected void test_findAll_is_empty() {
        assertEquals(carArrayDao.findAll(), new ArrayList<Car>(), "Initial list should be empty");
    }

    @Test
    protected void test_findById_is_null() {
        assertEquals(carArrayDao.findById(""), null, "Should return because that item is not in db");
    }

    @Test
    protected void test_delete_not_exists() {
        assertFalse(carArrayDao.delete(""), "Should return false");
    }

    @Test
    protected void test_existById_not_exists() {
        assertFalse(carArrayDao.existsById(""), "Should return false");
    }

    @Test
    protected void test_findById_success() {
        List<Car> cars = carArrayDao.getCars();
        cars.add(new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE));

        Car car = carArrayDao.findById(TEST_ID);

        assertNotNull(car);
        assertEquals(car.getId(), TEST_ID);
        assertEquals(car.getBrand(), TEST_BRAND);
        assertEquals(car.getModel(), TEST_MODEL);
        assertEquals(car.getPlate(), TEST_PLATE);
        assertEquals(car.getYear(), TEST_YEAR);
    }

    @Test
    protected void test_update_success() {
        List<Car> cars = carArrayDao.getCars();
        cars.add(new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE));
        Car carUpdate = new Car(TEST_ID, "BRAND_UPDATE", "MODEL_UPDATE", 2023, "PLATE_UPDATE");
        assertTrue(carArrayDao.update(carUpdate));
        Car carUpdateResult = cars.get(0);
        assertEquals(carUpdate, carUpdateResult);
        assertEquals(carUpdate.getId(), carUpdateResult.getId());
        assertEquals(carUpdate.getBrand(), carUpdateResult.getBrand());
        assertEquals(carUpdate.getModel(), carUpdateResult.getModel());
        assertEquals(carUpdate.getPlate(), carUpdateResult.getPlate());
        assertEquals(carUpdate.getYear(), carUpdateResult.getYear());

    }

    @Test
    protected void test_update_not_exists() {
        assertFalse(carArrayDao.update(new Car()));
    }

    @Test
    protected void test_save_success() {
        List<Car> cars = carArrayDao.getCars();
        Car carSave = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        assertTrue(carArrayDao.save(carSave));
        Car carSaveResult = cars.get(0);
        assertEquals(carSave, carSaveResult);
        assertEquals(carSave.getId(), carSaveResult.getId());
        assertEquals(carSave.getBrand(), carSaveResult.getBrand());
        assertEquals(carSave.getModel(), carSaveResult.getModel());
        assertEquals(carSave.getPlate(), carSaveResult.getPlate());
        assertEquals(carSave.getYear(), carSaveResult.getYear());
    }

    @Test
    protected void test_delete_success() {
        List<Car> cars = carArrayDao.getCars();
        Car carDelete = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        cars.add(carDelete);
        assertTrue(carArrayDao.delete(carDelete.getId()));
        assertEquals(cars.size(), 0);
    }

    @Test
    protected void test_existById_success() {
        List<Car> cars = carArrayDao.getCars();
        Car carExistsById = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);
        cars.add(carExistsById);
        assertTrue(carArrayDao.existsById(carExistsById.getId()));
    }

}
