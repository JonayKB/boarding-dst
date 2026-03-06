package it.dst.garage.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import it.dst.garage.model.entity.CarEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

class CarJpaRepositoryImplTest {

    private ICarJpaRepository internalJpaRepository;
    private CarJpaRepositoryImpl carRepository;

    @BeforeEach
    void setUp() {
        internalJpaRepository = Mockito.mock(ICarJpaRepository.class);
        carRepository = new CarJpaRepositoryImpl(internalJpaRepository);
    }

    @Test
    void testSave_ShouldReturnTrue_WhenSaveSuccessful() {
        CarEntity car = new CarEntity();
        car.setId("123");

        when(internalJpaRepository.save(car)).thenReturn(car);

        CarEntity result = carRepository.save(car);

        assertNotNull(result);
        assertEquals(car, result);
        verify(internalJpaRepository, times(1)).save(car);
    }

    @Test
    void testUpdate_ShouldReturnFalse_WhenCarDoesNotExist() {
        CarEntity car = new CarEntity();
        car.setId("NOT_FOUND");

        when(internalJpaRepository.existsById("NOT_FOUND")).thenReturn(false);

        CarEntity result = carRepository.update(car);

        assertNull(result);
        verify(internalJpaRepository, never()).save(any());
    }

    @Test
    void testFindById_ShouldReturnEntity() {
        CarEntity car = new CarEntity();
        when(internalJpaRepository.findById("1")).thenReturn(Optional.of(car));

        CarEntity result = carRepository.findById("1");

        assertNotNull(result);
        assertEquals(car, result);
    }

    @Test
    void testFindById_ShouldReturnNull_WhenNotFound() {
        when(internalJpaRepository.findById("NOT_FOUND")).thenReturn(Optional.empty());
        CarEntity result = carRepository.findById("NOT_FOUND");
        assertNull(result);
    }

    @Test
    void testFindAll_ShouldReturnList() {
        CarEntity car1 = new CarEntity();
        CarEntity car2 = new CarEntity();
        when(internalJpaRepository.findAll()).thenReturn(List.of(car1, car2));

        List<CarEntity> result = carRepository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(car1));
        assertTrue(result.contains(car2));
    }

    @Test
    void testUpdate_ShouldReturnTrue_WhenCarExists() {
        CarEntity car = new CarEntity();
        car.setId("123");

        when(internalJpaRepository.existsById("123")).thenReturn(true);
        when(internalJpaRepository.save(car)).thenReturn(car);

        CarEntity result = carRepository.update(car);

        assertNotNull(result);
        assertEquals(car, result);
        verify(internalJpaRepository, times(1)).save(car);
    }

    @Test
    void testDeleteById_ShouldReturnFalse_WhenCarDoesNotExist() {
        when(internalJpaRepository.existsById("NOT_FOUND")).thenReturn(false);
        boolean result = carRepository.deleteById("NOT_FOUND");
        assertFalse(result);
        verify(internalJpaRepository, never()).deleteById(any());
    }

    @Test
    void testDeleteById_ShouldReturnTrue_WhenCarExists() {
        when(internalJpaRepository.existsById("123")).thenReturn(true);
        boolean result = carRepository.deleteById("123");
        assertTrue(result);
        verify(internalJpaRepository, times(1)).deleteById("123");
    }

    @Test
    void testExistsById_ShouldReturnTrue_WhenCarExists() {
        when(internalJpaRepository.existsById("123")).thenReturn(true);
        boolean result = carRepository.existsById("123");
        assertTrue(result);
    }
}