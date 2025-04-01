package it.florenceConsulting.service;

import it.florenceConsulting.dto.UserDto;
import org.apache.coyote.BadRequestException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<UserDto> getUserByNameSurname(String name, String surname) throws BadRequestException;

    void uploadFile(MultipartFile file) throws BadRequestException;
}
