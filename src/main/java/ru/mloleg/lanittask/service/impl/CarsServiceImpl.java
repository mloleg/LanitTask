package ru.mloleg.lanittask.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mloleg.lanittask.dto.CreateCarRequest;
import ru.mloleg.lanittask.exception.IdAlreadyInUseException;
import ru.mloleg.lanittask.exception.PersonNotFoundException;
import ru.mloleg.lanittask.exception.UnderageOwnerException;
import ru.mloleg.lanittask.model.Person;
import ru.mloleg.lanittask.repository.CarsRepository;
import ru.mloleg.lanittask.repository.PeopleRepository;
import ru.mloleg.lanittask.service.CarsService;
import ru.mloleg.lanittask.util.mapper.CarsMapper;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarsServiceImpl implements CarsService {

    private final PeopleRepository peopleRepository;
    private final CarsRepository carsRepository;
    private final CarsMapper carsMapper;

    @Override
    public void createCar(CreateCarRequest request) {
        if (carsRepository.existsById(request.id())) {
            throw new IdAlreadyInUseException("ID {%d} машины уже используется.".formatted(request.id()));
        }

        Optional<Person> person = peopleRepository.findById(request.ownerId());

        if (person.isPresent()) {
            if (Period.between(person.get().getBirthdate(), LocalDate.now()).getYears() < 18) {
                throw new UnderageOwnerException("Владелец младше 18 лет.");
            }

            carsRepository.save(carsMapper.toCar(request));
        } else {
            throw new PersonNotFoundException("ID {%d} человека не найден.".formatted(request.ownerId()));
        }
    }
}
