package it.florenceConsulting.controller;

import it.florenceConsulting.dto.UserDto;
import it.florenceConsulting.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "surname", required = false) String surname) throws BadRequestException {
        //log.info("getUsers");

        List<UserDto> userDtoList = userService.getUserByNameSurname(name, surname);

        return new ResponseEntity<>(userDtoList, HttpStatusCode.valueOf(200));
    }

    @PostMapping("/import-file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws BadRequestException {
        //log.info("getUsers");

        userService.uploadFile(file);

        return new ResponseEntity<>("File uccessfully uploaded!", HttpStatusCode.valueOf(200));
    }
}
