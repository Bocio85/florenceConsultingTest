package it.florenceConsulting.service.impl;

import it.florenceConsulting.entity.User;
import it.florenceConsulting.mapper.UserMapper;
import it.florenceConsulting.repository.UserRepository;
import it.florenceConsulting.service.UserService;
import it.florenceConsulting.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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

    @Override
    public void uploadFile(MultipartFile file) throws BadRequestException {

        List<UserDto> userDtoList = new ArrayList<>();
        String parser =";";
        List<String> columns;
        List<List<String>> values;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String firstLine=br.readLine();
            if(firstLine==null) throw new BadRequestException("File is empty");
            columns = Arrays.asList(firstLine.split(parser));
            values = br.lines()
                    .map(line -> Arrays.asList(line.split(parser)))
                    .collect(Collectors.toList());

            userDtoList = convertToDto(columns, values);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(!userDtoList.isEmpty()) {
            validateUserList(userDtoList);
            saveUserList(userDtoList);
        }

    }

    private List<UserDto> convertToDto(List<String> columns, List<List<String>> values) {

        return values.stream().map(this::rowToUserDto).toList();
    }

    private UserDto rowToUserDto(List<String> row) {
        return UserDto.builder()
                .name(row.get(0))
                .surname(row.get(1))
                .uniqueCode(row.get(2))
                .contactNumber(row.get(3))
                .address(row.get(4))
                .city(row.get(5))
                .zipCode(row.get(6))
                .email(row.get(7))
                .build();
    }

    private void validateUserList(List<UserDto> userDtoList) throws BadRequestException {

        // validation 1 - empty mandatory fields
        List<String> errorMessageList = userDtoList.stream().map(userDto -> {
            String errorMessage = "";
            if(userDto.getName() == null || userDto.getName().isEmpty())
                errorMessage += "Empty mandatory field name ";
            if(userDto.getSurname() == null || userDto.getSurname().isEmpty())
                errorMessage += "Empty mandatory field surname ";
            if(userDto.getUniqueCode() == null || userDto.getUniqueCode().isEmpty())
                errorMessage += "Empty mandatory field unique_code ";
            return errorMessage;
        }).toList();
        if(errorMessageList.isEmpty())
            throw new BadRequestException("errorMessageList");

        // validation 2 - already present user
        List<String> uniqueCodeList = userDtoList.stream().map(UserDto::getUniqueCode).toList();
        List<User> userAlreadyPresent = userRepository.findByUniqueCodeIn(uniqueCodeList);
        if(!userAlreadyPresent.isEmpty()) {
            String uniqueCodePresentList = userAlreadyPresent.stream().map(User::getUniqueCode).toString();
            throw new BadRequestException("User with unique_code " + uniqueCodePresentList + " already present");
        }
    }

    private void saveUserList(List<UserDto> userDtoList) {

        List<User> userList = userDtoList.stream().map(UserMapper.ISTANCE::dtoToEntity).toList();
        userRepository.saveAll(userList);
    }


}
