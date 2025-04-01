package it.florenceConsulting.mapper;

import it.florenceConsulting.dto.UserDto;
import it.florenceConsulting.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    final UserMapper ISTANCE = Mappers.getMapper(UserMapper.class);

    UserDto entityToDto(User user);

    User dtoToEntity(UserDto userDto);

}
