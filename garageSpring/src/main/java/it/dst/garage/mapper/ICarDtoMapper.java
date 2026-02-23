package it.dst.garage.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import it.dst.garage.model.Car;
import it.dst.garage.model.dto.CarDto;

@Mapper(componentModel = "spring")
public interface ICarDtoMapper {
    CarDto toDto(Car car);

    Car toModel(CarDto carDto);

    List<CarDto> toDto(List<Car> cars);

    List<Car> toModel(List<CarDto> carDtos);

}
