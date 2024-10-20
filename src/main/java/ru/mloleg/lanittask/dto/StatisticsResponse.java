package ru.mloleg.lanittask.dto;


import lombok.Builder;

@Builder
public record StatisticsResponse(
        Long peopleCount,
        Long carsCount,
        Long uniqueVendorsCount) {

}
