package it.dst.garage.seed;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import it.dst.garage.mapper.ICarEntityMapper;
import it.dst.garage.mapper.ICarEntityMapperImpl;
import it.dst.garage.repository.ICarRepository;

public class CarSeederTest {

    private CarSeeder carSeeder;
    private static final String JSON = """

            [

            {"id":"1", "brand":"Fiat", "model":"500", "year":2022, "plate":"AA111AA"},

            {"id":"2", "brand":"Tesla", "model":"Model 3", "year":2023, "plate":"BB222BB"},

            {"id":"3", "brand":"Ford", "model":"Focus", "year":2021, "plate":"CC333CC"}

            ]

            """;
    @Mock
    private Resource resourceMock;

    @Mock
    private ICarRepository carRepository;

    private final ICarEntityMapper carEntityMapper = new ICarEntityMapperImpl();

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
        carSeeder = new CarSeeder(carRepository, carEntityMapper);

        ReflectionTestUtils.setField(carSeeder, "carsJsonResource", resourceMock);
    }

    @Test
    void test_seed_database() throws IOException {
        when(resourceMock.getInputStream()).thenReturn(new ByteArrayInputStream(JSON.getBytes()));

        carSeeder.seed();

        verify(carRepository, times(3)).save(any());

    }
}