package it.florenceConsulting.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    Integer id;
    String name;
    String surname;
    String uniqueCode;
    String contactNumber;
    String address;
    String city;
    String zipCode;
    String email;
}
