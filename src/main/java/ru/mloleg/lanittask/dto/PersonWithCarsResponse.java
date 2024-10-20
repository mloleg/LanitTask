package ru.mloleg.lanittask.dto;

import lombok.Builder;
import ru.mloleg.lanittask.model.Car;

import java.time.LocalDate;
import java.util.List;

@Builder
public record PersonWithCarsResponse(
        Long id,
        String name,
        LocalDate birthdate,
        List<Car> carList) {

}
