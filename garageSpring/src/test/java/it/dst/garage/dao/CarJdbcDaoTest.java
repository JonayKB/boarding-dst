package it.dst.garage.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import it.dst.garage.model.Car;
import it.dst.garage.utils.IConnectionProvider;

public class CarJdbcDaoTest {

    private static final String TEST_PLATE = "TEST_PLATE";

    private static final int TEST_YEAR = 2020;

    private static final String TEST_MODEL = "TEST_MODEL";

    private static final String TEST_BRAND = "TEST_BRAND";

    private static final String TEST_ID = "TEST_ID";

    private CarJdbcDao carJdbcDao;

    @Mock
    private IConnectionProvider connectionProvider;

    private Connection connection;
    private static final String DB_PATH = "./target/testdb";

    @BeforeEach
    void beforeEach() throws SQLException {
        MockitoAnnotations.openMocks(this);

        String url = "jdbc:h2:" + DB_PATH + ";AUTO_SERVER=TRUE";

        when(connectionProvider.getConnection()).thenAnswer(inv -> DriverManager.getConnection(url));

        carJdbcDao = new CarJdbcDao(connectionProvider);
    }

    @AfterEach
    void afterEach() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.createStatement().execute("SHUTDOWN");
            connection.close();
        }
    }

    @AfterAll
    static void afterAll() {
        try {
            Path path = Paths.get(DB_PATH + ".mv.db");
            Files.deleteIfExists(path);

            Files.deleteIfExists(Paths.get(DB_PATH + ".trace.db"));

            System.out.println("Database files cleaned up successfully.");
        } catch (Exception e) {
            System.err.println("Could not delete database files: " + e.getMessage());
        }
    }

    @Test
    protected void test_findAll_empty() {
        assertTrue(carJdbcDao.findAll().isEmpty());
    }

    @Test
    protected void test_save_findById_update_delete() throws SQLException {
        Car car = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);

        assertTrue(carJdbcDao.save(car));

        Car carFindById = carJdbcDao.findById(TEST_ID);

        assertNotNull(carFindById);

        assertEquals(car, carFindById);
        assertEquals(car.getId(), carFindById.getId());
        assertEquals(car.getModel(), carFindById.getModel());
        assertEquals(car.getBrand(), carFindById.getBrand());
        assertEquals(car.getPlate(), carFindById.getPlate());
        assertEquals(car.getYear(), carFindById.getYear());

        Car carUpdate = new Car(TEST_ID, TEST_BRAND, "UPDATE_MODEL", TEST_YEAR, TEST_PLATE);
        assertTrue(carJdbcDao.update(carUpdate));
        assertEquals(car, carFindById);
        assertEquals(carUpdate.getId(), carFindById.getId());
        assertNotEquals(carUpdate.getModel(), carFindById.getModel());
        assertEquals(carUpdate.getBrand(), carFindById.getBrand());
        assertEquals(carUpdate.getPlate(), carFindById.getPlate());
        assertEquals(carUpdate.getYear(), carFindById.getYear());

        assertTrue(carJdbcDao.delete(TEST_ID));

        assertNull(carJdbcDao.findById(TEST_ID));
    }
}
