package it.dst.garage.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;

import it.dst.garage.controller.v1.RestCarControllerV1;
import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.mapper.v1.ICarDtoV1Mapper;
import it.dst.garage.mapper.v1.ICarDtoV1MapperImpl;
import it.dst.garage.model.Car;
import it.dst.garage.model.dto.v1.CarDtoV1;
import it.dst.garage.model.request.CarFilterRequest;
import it.dst.garage.model.request.CarSortRequest;
import it.dst.garage.model.dto.v1.CarDtoNoIdV1;
import it.dst.garage.proxy.CarProxy;

class RestControllerTest {
    private RestCarControllerV1 restCarController;
    @Mock
    private CarProxy carProxy;
    private ICarDtoV1Mapper carDtoMapper = new ICarDtoV1MapperImpl();

    private Car car1;
    private Car car2;
    private Car car3;

    private Page<Car> cars;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
        restCarController = new RestCarControllerV1(carProxy, carDtoMapper);
        car1 = new Car("1", "Toyota", "Corolla", 2020, "AB1234");
        car2 = new Car("2", "Honda", "Civic", 2019, "CD5678");
        car3 = new Car("3", "Ford", "Focus", 2018, "EF9012");
        cars = new PageImpl<>(List.of(car1, car2, car3));

    }

    @Test
    void test_findAll() {
        when(carProxy.findAll(anyInt(), anyInt(), anyMap(), any(Sort.class))).thenReturn(cars);
        Page<CarDtoV1> resultPage = restCarController.findAll(0, new CarFilterRequest(), new CarSortRequest()).getBody();
        assertNotNull(resultPage);
        List<CarDtoV1> result = resultPage.getContent();
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Toyota", result.get(0).getBrand());
        assertEquals("Honda", result.get(1).getBrand());
        assertEquals("Ford", result.get(2).getBrand());
    }

    @Test
    void test_findById() {
        when(carProxy.findById("1")).thenReturn(car1);
        CarDtoV1 result = restCarController.findById("1").getBody();
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
        CarDtoNoIdV1 carDtoNoId = new CarDtoNoIdV1("Nissan", "Altima", 2021, "GH3456");
        when(carProxy.existsById(any())).thenReturn(false);
        when(carProxy.save(any())).thenReturn(true);
        assertEquals(200, restCarController.save(carDtoNoId).getStatusCode().value());
    }

    @Test
    void test_save_unvalid() throws UnvalidCarException {
        CarDtoNoIdV1 carDtoNoId = new CarDtoNoIdV1("", "Altima", 2021, "GH3456");
        when(carProxy.existsById(any())).thenReturn(false);
        when(carProxy.save(any())).thenThrow(new UnvalidCarException("Car has not unique id"));
        assertThrows(UnvalidCarException.class, () -> restCarController.save(carDtoNoId));
    }

    @Test
    void test_save_internalError() throws UnvalidCarException {
        CarDtoNoIdV1 carDtoNoId = new CarDtoNoIdV1("Nissan", "Altima", 2021, "GH3456");
        when(carProxy.existsById(any())).thenReturn(false);
        when(carProxy.save(any())).thenReturn(false);
        assertEquals(500, restCarController.save(carDtoNoId).getStatusCode().value());
    }

    @Test
    void test_update() throws UnvalidCarException {
        CarDtoNoIdV1 carDtoNoId = new CarDtoNoIdV1("Nissan", "Altima", 2021, "GH3456");
        when(carProxy.update(any())).thenReturn(true);
        assertEquals(200, restCarController.update("1", carDtoNoId).getStatusCode().value());
    }

    @Test
    void test_update_unvalid() throws UnvalidCarException {
        CarDtoNoIdV1 carDtoNoId = new CarDtoNoIdV1("", "Altima", 2021, "GH3456");
        when(carProxy.update(any())).thenThrow(new UnvalidCarException("Car has not unique id"));
        assertThrows(UnvalidCarException.class, () -> restCarController.update("1", carDtoNoId));
    }

    @Test
    void test_update_internalError() throws UnvalidCarException {
        CarDtoNoIdV1 carDtoNoId = new CarDtoNoIdV1("Nissan", "Altima", 2021, "GH3456");
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