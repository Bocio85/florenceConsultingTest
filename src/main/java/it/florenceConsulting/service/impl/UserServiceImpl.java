package it.florenceConsulting.service.impl;

import it.florenceConsulting.entity.User;
import it.florenceConsulting.exception.BadRequestException;
import it.florenceConsulting.mapper.UserMapper;
import it.florenceConsulting.repository.UserRepository;
import it.florenceConsulting.service.UserService;
import it.florenceConsulting.dto.UserDto;
import it.florenceConsulting.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static it.florenceConsulting.util.Constants.columenNumber;
import static it.florenceConsulting.util.Constants.csvParser;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Get users
     * @param name
     * @param surname
     * @return a list of UserDto
     * @throws BadRequestException
     */
    @Override
    public List<UserDto> getUserByNameSurname(String name, String surname) throws BadRequestException {

        List<User> users = userRepository.findByNameSurname(name, surname);

        return users.stream().map(UserMapper.ISTANCE::entityToDto).toList();

    }

    /**
     * Upload users by csv file
     * @param file
     * @throws BadRequestException
     */
    @Override
    public void uploadFile(MultipartFile file) throws BadRequestException {

        List<UserDto> userDtoList = new ArrayList<>();
        // convert csv to UserDto list
        userDtoList = fileToUserDtoList(file);

        if(!userDtoList.isEmpty()) {
            validateUserList(userDtoList);
            saveUserList(userDtoList);
        } else {
            log.info("Empty file");
        }

    }

    private List<UserDto> fileToUserDtoList(MultipartFile file) throws BadRequestException {
        List<UserDto> userDtoList;
        List<String> columns;
        List<List<String>> values;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String firstLine=br.readLine();
            if(firstLine==null) throw new BadRequestException("File is empty");
            columns = Arrays.asList(firstLine.split(csvParser));
            values = br.lines()
                    .map(line -> Arrays.asList(line.split(csvParser)))
                    .collect(Collectors.toList());

            userDtoList = convertToDto(columns, values);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userDtoList;
    }

    private List<UserDto> convertToDto(List<String> columns, List<List<String>> values) throws BadRequestException {

        validateColumns(columns);

        validateRows(values);

        return values.stream().map(this::rowToUserDto).toList();
    }

    private void validateRows(List<List<String>> values) throws BadRequestException {
        Optional<List<String>> notCompletedRowOpt = values.stream()
                .filter(row-> row.size()!= columenNumber )
                .findFirst();

        if(notCompletedRowOpt.isPresent()) {
            throw new BadRequestException("One or more rows have more or less columns then the expected " + columenNumber + " columns");
        }
    }

    private void validateColumns(List<String> columns) throws BadRequestException {

        if(!columns.equals(Constants.userFileColumns)) {
            throw new BadRequestException("Wrong CSV format. Expected " + Constants.userFileColumns);
        }
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

        // validation 1 - check empty mandatory fields
        List<String> errorMessageList = userDtoList.stream().map(userDto -> {
            String errorMessage = "";
            if(userDto.getName() == null || userDto.getName().isEmpty())
                errorMessage += "Empty mandatory field name ";
            if(userDto.getSurname() == null || userDto.getSurname().isEmpty())
                errorMessage += "Empty mandatory field surname ";
            if(userDto.getUniqueCode() == null || userDto.getUniqueCode().isEmpty())
                errorMessage += "Empty mandatory field unique_code ";
            return errorMessage;
        })
                .filter(m -> !m.isEmpty())
                .toList();
        if(!errorMessageList.isEmpty()) {
            throw new BadRequestException(""+ errorMessageList);
        }

        // validation 2 - check duplicated user in file
        List<String> uniqueCodeList = userDtoList.stream().map(UserDto::getUniqueCode).toList();
        List<String> uniqueCodeListDuplicated =
                uniqueCodeList.stream()
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                        .entrySet()
                        .stream()
                        .filter(m -> m.getValue() > 1)
                        .map(Map.Entry::getKey)
                        .toList();
        if(!uniqueCodeListDuplicated.isEmpty()) {
            throw new BadRequestException("Duplicated user with unique_code " + uniqueCodeListDuplicated + " in to the file");
        }

        // validation 3 - check user already present
        List<User> userAlreadyPresent = userRepository.findByUniqueCodeIn(uniqueCodeList);
        if(!userAlreadyPresent.isEmpty()) {
            List<String> uniqueCodePresentList = userAlreadyPresent.stream().map(User::getUniqueCode).toList();
            throw new BadRequestException("User with unique_code " + uniqueCodePresentList + " already present");
        }
    }

    private void saveUserList(List<UserDto> userDtoList) {

        List<User> userList = userDtoList.stream().map(UserMapper.ISTANCE::dtoToEntity).toList();
        userRepository.saveAll(userList);
    }


}
