package it.florenceConsulting.entity;

import jakarta.persistence.*;
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
    @SequenceGenerator(name="user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
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
