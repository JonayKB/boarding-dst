package it.dst.garage.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

import it.dst.garage.model.Car;
import it.dst.garage.model.entity.CarEntity;

@Mapper(componentModel = "spring")
public interface ICarEntityMapper {
    CarEntity toEntity(Car car);

    Car toModel(CarEntity carEntity);

    List<CarEntity> toEntity(List<Car> cars);

    List<Car> toModel(List<CarEntity> carEntities);

    default Page<Car> toModel(Page<CarEntity> carEntities) {
        return carEntities.map(this::toModel);
    }

}
