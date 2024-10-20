package ru.mloleg.lanittask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.mloleg.lanittask.model.Car;
import ru.mloleg.lanittask.model.Person;

import java.util.List;

public interface CarsRepository extends JpaRepository<Car, Long> {

    @Query("SELECT COUNT(DISTINCT c) FROM Car c")
    long countDistinctCars();

    @Query(value = "SELECT COUNT(DISTINCT LOWER(SPLIT_PART(c.model, '-', 1))) FROM Car c", nativeQuery = true)
    long countDistinctVendors();

    List<Car> findByOwner(Person owner);
}
