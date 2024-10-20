package ru.mloleg.lanittask.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mloleg.lanittask.dto.CreatePersonRequest;
import ru.mloleg.lanittask.exception.IdAlreadyInUseException;
import ru.mloleg.lanittask.repository.PeopleRepository;
import ru.mloleg.lanittask.service.PeopleService;
import ru.mloleg.lanittask.util.mapper.PeopleMapper;

@Service
@RequiredArgsConstructor
public class PeopleServiceImpl implements PeopleService {

    private final PeopleRepository peopleRepository;
    private final PeopleMapper peopleMapper;

    @Override
    public void createPerson(CreatePersonRequest request) {
        if (peopleRepository.existsById(request.id())) {
            throw new IdAlreadyInUseException("ID {%d} уже используется.".formatted(request.id()));
        }

        peopleRepository.save(peopleMapper.toPerson(request));
    }
}
