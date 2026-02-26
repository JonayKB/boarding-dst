package it.dst.garage.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.dst.garage.exceptions.UnvalidCarException;
import it.dst.garage.mapper.ICarDtoMapper;
import it.dst.garage.model.Car;
import it.dst.garage.model.dto.CarDto;
import it.dst.garage.model.dto.CarDtoNoId;
import it.dst.garage.proxy.CarProxy;
import jakarta.validation.Valid;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/cars")
@Tag(name = "Car API", description = "Endpoints for managing cars in the garage")
public class RestCarController {

    private CarProxy carProxy;
    private static final int PAGE_SIZE = 5;
    private ICarDtoMapper carDtoMapper;

    public RestCarController(CarProxy carProxy, ICarDtoMapper carDtoMapper) {
        this.carProxy = carProxy;
        this.carDtoMapper = carDtoMapper;
    }

    @GetMapping("/")
    @Operation(summary = "Get all cars with pagination", description = "Returns a paginated list of cars in the garage")
    public ResponseEntity<Page<CarDto>> findAll(@RequestParam(defaultValue = "0") Integer page) {
        Page<Car> cars = carProxy.findAll(page, PAGE_SIZE);
        return ResponseEntity.ok(cars.map(carDtoMapper::toDto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a car by ID", description = "Returns a car in the garage with the given ID")
    public ResponseEntity<CarDto> findById(@PathVariable String id) {
        Car car = carProxy.findById(id);
        if (car == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(carDtoMapper.toDto(car));
    }

    @PostMapping("/")
    @Operation(summary = "Add a new car", description = "Adds a new car to the garage")
    public ResponseEntity<String> save(@Valid @RequestBody CarDtoNoId carDtoNoId) throws UnvalidCarException {
        String id;
        do {
            id = UUID.randomUUID().toString();
        } while (carProxy.existsById(id));
        Car car = carDtoMapper.toModel(new CarDto(id, carDtoNoId.getBrand(), carDtoNoId.getModel(),
                carDtoNoId.getYear(), carDtoNoId.getPlate()));

        boolean status = carProxy.save(car);
        if (status) {
            return ResponseEntity.ok("Car with id: " + car.getId() + " has been saved correctly");
        } else {
            return ResponseEntity.status(500).body("Failed to add car");
        }

    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing car", description = "Updates the details of an existing car in the garage")
    public ResponseEntity<String> update(@PathVariable String id, @Valid @RequestBody CarDtoNoId carDtoNoId)
            throws UnvalidCarException {
        Car car = carDtoMapper.toModel(new CarDto(id, carDtoNoId.getBrand(), carDtoNoId.getModel(),
                carDtoNoId.getYear(), carDtoNoId.getPlate()));

        boolean status = carProxy.update(car);
        if (status) {
            return ResponseEntity.ok("Car with id: " + car.getId() + " has been updated correctly");
        } else {
            return ResponseEntity.status(500).body("Failed to update car");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a car", description = "Deletes a car from the garage with the given ID")
    public ResponseEntity<String> delete(@PathVariable String id) {
        boolean status = carProxy.delete(id);
        if (status) {
            return ResponseEntity.ok("Car deleted successfully");
        } else {
            return ResponseEntity.status(400).body("Car with id " + id + " not found");
        }
    }

}
