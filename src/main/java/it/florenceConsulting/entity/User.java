package it.florenceConsulting.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    Integer id;

    String name;

    String surname;

    @Column(name="unique_code")
    String uniqueCode;

    @Column(name="contact_number")
    String contactNumber;

    String address;

    String city;

    @Column(name="zip_code")
    String zipCode;

    String email;
}
