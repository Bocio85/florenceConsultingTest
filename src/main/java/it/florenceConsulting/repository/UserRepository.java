package it.florenceConsulting.repository;
//import org.springframework.da

import it.florenceConsulting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select u.* from users u " +
            "where case when cast(?1 as varchar) is null then true else \"name\" = cast(?1 as varchar) end " +
            "and case when cast(?2 as varchar) is null then true else surname = cast(?2 as varchar) end ",
            nativeQuery = true)
    List<User> findByNameSurname(String name, String surname);

    List<User> findByUniqueCodeIn(List<String> uniqueCode);

}
