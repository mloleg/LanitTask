package ru.mloleg.lanittask.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.mloleg.lanittask.dto.CreatePersonRequest;
import ru.mloleg.lanittask.model.Person;

@Component
@Mapper(componentModel = "spring")
public interface PeopleMapper {

    @Mapping(target = "birthdate", dateFormat = "dd.MM.yyyy")
    Person toPerson(CreatePersonRequest request);
}
