package ru.mloleg.lanittask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.mloleg.lanittask.model.Person;

public interface PeopleRepository extends JpaRepository<Person, Long> {

    @Query("SELECT COUNT(DISTINCT p) FROM Person p")
    long countDistinctPeople();
}
