package it.florenceConsulting.service.impl;

import it.florenceConsulting.entity.User;
import it.florenceConsulting.mapper.UserMapper;
import it.florenceConsulting.repository.UserRepository;
import it.florenceConsulting.service.UserService;
import it.florenceConsulting.dto.UserDto;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDto> getUserByNameSurname(String name, String surname) throws BadRequestException {

        if(name == null && surname == null)
            throw new BadRequestException("name and surname can be not both null!");

        List<User> users = userRepository.findByNameSurname(name, surname);

        return users.stream().map(UserMapper.ISTANCE::entityToDto).toList();

    }
}
