package it.florenceConsulting.service;

import it.florenceConsulting.dto.UserDto;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface UserService {

    List<UserDto> getUserByNameSurname(String name, String surname) throws BadRequestException;
}
