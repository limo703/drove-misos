package ru.alexashin.misosdrove2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.alexashin.misosdrove2.models.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person,Integer> {
    Optional<Person> findByCarNumber(String carNumber);
    List<Person> findPeopleByUsernameStartingWith(String query);
}
