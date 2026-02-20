package it.dst.garage.seed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.test.util.ReflectionTestUtils;

import it.dst.garage.dao.CarDaoFactory;
import it.dst.garage.dao.CarMapDao;
import it.dst.garage.exceptions.PersistanceTypeException;
import it.dst.garage.model.Car;

public class CarSeederTest {
    @Mock
    private CarDaoFactory carDaoFactory;

    private CarSeeder carSeeder;

    @Mock
    private Resource resourceMock;

    private CarMapDao carDao;
    private static final String JSON = """
            [
                {"id":"1", "brand":"Fiat", "model":"500", "year":2022, "plate":"AA111AA"},
                {"id":"2", "brand":"Tesla", "model":"Model 3", "year":2023, "plate":"BB222BB"},
                {"id":"3", "brand":"Ford", "model":"Focus", "year":2021, "plate":"CC333CC"}
            ]
            """;

    @BeforeEach
    protected void beforeEach() throws SQLException, PersistanceTypeException, IOException {
        MockitoAnnotations.openMocks(this);
        carDao = new CarMapDao();
        when(carDaoFactory.create()).thenReturn(carDao);
        carSeeder = new CarSeeder(carDaoFactory);
        ReflectionTestUtils.setField(carSeeder, "carsJsonResource", resourceMock);

    }

    @Test
    protected void test_seed_database() throws IOException {
        when(resourceMock.getInputStream()).thenAnswer(inv -> new ByteArrayInputStream(JSON.getBytes()));

        carSeeder.seed();
        assertEquals(3, carDao.findAll().size());
    }

    @Test
    protected void test_seed_car_already_exists() throws IOException {
        when(resourceMock.getInputStream()).thenAnswer(inv -> new ByteArrayInputStream(JSON.getBytes()));
        carDao.save(new Car("1", "JSON", "JSON", 0, "JSON"));

        carSeeder.seed();
        assertEquals(3, carDao.findAll().size());
    }
}
