package ru.mloleg.lanittask.util.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.mloleg.lanittask.dto.CreateCarRequest;
import ru.mloleg.lanittask.model.Car;
import ru.mloleg.lanittask.model.Person;

@Component
@Mapper(componentModel = "spring")
public interface CarsMapper {

    Car toCar(CreateCarRequest request);

    default Person map(Long value) {
        Person person = new Person();
        person.setId(value);
        return person;
    }
}
