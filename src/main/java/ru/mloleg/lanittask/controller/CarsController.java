package ru.mloleg.lanittask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mloleg.lanittask.dto.CreateCarRequest;
import ru.mloleg.lanittask.dto.common.CommonRequest;
import ru.mloleg.lanittask.service.CarsService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/car")
public class CarsController {

    private final CarsService carsService;

    @PostMapping
    public void create(@RequestBody @Valid CommonRequest<CreateCarRequest> request) {
        carsService.createCar(request.getBody());
    }
}
