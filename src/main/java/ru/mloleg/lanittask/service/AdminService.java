package ru.mloleg.lanittask.service;

import ru.mloleg.lanittask.dto.PersonWithCarsResponse;
import ru.mloleg.lanittask.dto.StatisticsResponse;

public interface AdminService {

    PersonWithCarsResponse getPersonWithCars(Long id);

    StatisticsResponse getStatistics();

    void clear();
}
