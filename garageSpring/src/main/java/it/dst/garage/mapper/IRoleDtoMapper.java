package it.dst.garage.mapper;

import org.mapstruct.Mapper;

import it.dst.garage.model.Role;
import it.dst.garage.model.dto.RoleDto;

@Mapper(componentModel = "spring")
public interface IRoleDtoMapper {
    RoleDto toDto(Role role);

    Role toModel(RoleDto roleDto);

}
