package ru.mloleg.lanittask.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mloleg.lanittask.dto.PersonWithCarsResponse;
import ru.mloleg.lanittask.dto.StatisticsResponse;
import ru.mloleg.lanittask.exception.PersonNotFoundException;
import ru.mloleg.lanittask.model.Person;
import ru.mloleg.lanittask.repository.CarsRepository;
import ru.mloleg.lanittask.repository.PeopleRepository;
import ru.mloleg.lanittask.service.AdminService;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final CarsRepository carsRepository;
    private final PeopleRepository peopleRepository;

    @Override
    public PersonWithCarsResponse getPersonWithCars(Long id) {
        Person person = peopleRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(
                        "Не найден человек с ID {%d}.".formatted(id)));

        return PersonWithCarsResponse.builder()
                .id(id)
                .name(person.getName())
                .birthdate(person.getBirthdate())
                .carList(carsRepository.findByOwner(person))
                .build();
    }

    @Override
    public StatisticsResponse getStatistics() {
        return StatisticsResponse.builder()
                .peopleCount(peopleRepository.countDistinctPeople())
                .carsCount(carsRepository.countDistinctCars())
                .uniqueVendorsCount(carsRepository.countDistinctVendors())
                .build();
    }

    @Override
    public void clear() {
        carsRepository.deleteAll();
        peopleRepository.deleteAll();
    }
}
