package it.dst.garage.mapper;

import org.mapstruct.Mapper;

import it.dst.garage.model.User;
import it.dst.garage.model.dto.UserDto;

@Mapper(componentModel = "spring", uses = { IRoleDtoMapper.class })
public interface IUserDtoMapper {

    UserDto toDto(User user);
}
