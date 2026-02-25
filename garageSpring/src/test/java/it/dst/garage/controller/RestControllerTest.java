package it.dst.garage.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.mapper.ICarDtoMapper;
import it.dst.garage.mapper.ICarDtoMapperImpl;
import it.dst.garage.model.Car;
import it.dst.garage.model.dto.CarDto;
import it.dst.garage.model.dto.CarDtoNoId;
import it.dst.garage.proxy.CarProxy;

public class RestControllerTest {
    private RestCarController restCarController;
    @Mock
    private CarProxy carProxy;
    private ICarDtoMapper carDtoMapper = new ICarDtoMapperImpl();

    private Car car1;
    private Car car2;
    private Car car3;

    private Page<Car> cars;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
        restCarController = new RestCarController(carProxy, carDtoMapper);
        car1 = new Car("1", "Toyota", "Corolla", 2020, "AB1234");
        car2 = new Car("2", "Honda", "Civic", 2019, "CD5678");
        car3 = new Car("3", "Ford", "Focus", 2018, "EF9012");
        cars = new PageImpl<>(List.of(car1, car2, car3));

    }

    @Test
    void test_findAll() {
        when(carProxy.findAll(anyInt(), anyInt())).thenReturn(cars);
        Page<CarDto> resultPage = restCarController.findAll(0).getBody();
        assertNotNull(resultPage);
        List<CarDto> result = resultPage.getContent();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Toyota", result.get(0).getBrand());
        assertEquals("Honda", result.get(1).getBrand());
        assertEquals("Ford", result.get(2).getBrand());
    }

    @Test
    void test_findById() {
        when(carProxy.findById("1")).thenReturn(car1);
        CarDto result = restCarController.findById("1").getBody();
        assertNotNull(result);
        assertEquals("Toyota", result.getBrand());
        assertEquals("Corolla", result.getModel());
        assertEquals(2020, result.getYear());
        assertEquals("AB1234", result.getPlate());
    }

    @Test
    void test_findById_notFound() {
        when(carProxy.findById("4")).thenReturn(null);
        assertEquals(404, restCarController.findById("4").getStatusCode().value());
    }

    @Test
    void test_save() throws UnvalidCarException {
        CarDtoNoId carDtoNoId = new CarDtoNoId("Nissan", "Altima", 2021, "GH3456");
        when(carProxy.existsById(any())).thenReturn(false);
        when(carProxy.save(any())).thenReturn(true);
        assertEquals(200, restCarController.save(carDtoNoId).getStatusCode().value());
    }

    @Test
    void test_save_unvalid() throws UnvalidCarException {
        CarDtoNoId carDtoNoId = new CarDtoNoId("", "Altima", 2021, "GH3456");
        when(carProxy.existsById(any())).thenReturn(false);
        when(carProxy.save(any())).thenThrow(new UnvalidCarException("Car has not unique id"));
        assertEquals(400, restCarController.save(carDtoNoId).getStatusCode().value());
    }

    @Test
    void test_save_internalError() throws UnvalidCarException {
        CarDtoNoId carDtoNoId = new CarDtoNoId("Nissan", "Altima", 2021, "GH3456");
        when(carProxy.existsById(any())).thenReturn(false);
        when(carProxy.save(any())).thenReturn(false);
        assertEquals(500, restCarController.save(carDtoNoId).getStatusCode().value());
    }

    @Test
    void test_update() throws UnvalidCarException {
        CarDtoNoId carDtoNoId = new CarDtoNoId("Nissan", "Altima", 2021, "GH3456");
        when(carProxy.update(any())).thenReturn(true);
        assertEquals(200, restCarController.update("1", carDtoNoId).getStatusCode().value());
    }

    @Test
    void test_update_unvalid() throws UnvalidCarException {
        CarDtoNoId carDtoNoId = new CarDtoNoId("", "Altima", 2021, "GH3456");
        when(carProxy.update(any())).thenThrow(new UnvalidCarException("Car has not unique id"));
        assertEquals(400, restCarController.update("1", carDtoNoId).getStatusCode().value());
    }

    @Test
    void test_update_internalError() throws UnvalidCarException {
        CarDtoNoId carDtoNoId = new CarDtoNoId("Nissan", "Altima", 2021, "GH3456");
        when(carProxy.update(any())).thenReturn(false);
        assertEquals(500, restCarController.update("1", carDtoNoId).getStatusCode().value());
    }

    @Test
    void test_delete() {
        when(carProxy.delete("1")).thenReturn(true);
        assertEquals(200, restCarController.delete("1").getStatusCode().value());
    }

    @Test
    void test_delete_notFound() {
        when(carProxy.delete("4")).thenReturn(false);
        assertEquals(400, restCarController.delete("4").getStatusCode().value());
    }
}