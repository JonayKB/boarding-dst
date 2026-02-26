package it.dst.garage.mapper;

import org.mapstruct.Mapper;

import it.dst.garage.model.Role;
import it.dst.garage.model.entity.RoleEntity;


@Mapper(componentModel = "spring")
public interface IRoleEntityMapper {
    RoleEntity toEntity(Role role);
    Role toModel(RoleEntity roleEntity);
    
}
