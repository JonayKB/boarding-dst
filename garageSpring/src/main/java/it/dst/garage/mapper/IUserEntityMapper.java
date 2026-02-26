package it.dst.garage.mapper;

import org.mapstruct.Mapper;

import it.dst.garage.model.User;
import it.dst.garage.model.entity.UserEntity;

@Mapper(componentModel = "spring", uses = { IRoleEntityMapper.class })
public interface IUserEntityMapper {
    UserEntity toEntity(User user);

    User toModel(UserEntity userEntity);

}
