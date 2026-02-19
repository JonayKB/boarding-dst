package it.dst.garage.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    @BeforeEach
    void beforeEach() throws SQLException {
        MockitoAnnotations.openMocks(this);
        connection = DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");

        when(connectionProvider.getConnection())
                .thenAnswer(invocation -> DriverManager.getConnection("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1"));
        carJdbcDao = new CarJdbcDao(connectionProvider);

    }

    @AfterEach
    void afterEach() throws SQLException {
        if (connection != null) {
            connection.createStatement().execute("SHUTDOWN");
            connection.close();
        }

    }

    @Test
    protected void test_findAll_empty() {
        assertTrue(carJdbcDao.findAll().isEmpty());
    }

    // @Test
    // protected void test_save_and_findById() throws SQLException {
    // Car car = new Car(TEST_ID, TEST_BRAND, TEST_MODEL, TEST_YEAR, TEST_PLATE);

    // carJdbcDao.save(car);

    // Car carFindById = carJdbcDao.findById(TEST_ID);

    // assertNotNull(carFindById);

    // assertEquals(car, carFindById);
    // assertEquals(car.getId(), carFindById.getId());
    // assertEquals(car.getModel(), carFindById.getModel());
    // assertEquals(car.getBrand(), carFindById.getBrand());
    // assertEquals(car.getPlate(), carFindById.getPlate());
    // assertEquals(car.getYear(), carFindById.getYear());

    // }
}
