package it.dst.garage.mapper.v1;

import java.util.List;

import org.mapstruct.Mapper;

import it.dst.garage.model.Car;
import it.dst.garage.model.dto.v1.CarDtoV1;

@Mapper(componentModel = "spring")
public interface ICarDtoV1Mapper {
    CarDtoV1 toDto(Car car);
    Car toModel(CarDtoV1 carDto);

    List<CarDtoV1> toDto(List<Car> cars);

    List<Car> toModel(List<CarDtoV1> carDtos);
}
