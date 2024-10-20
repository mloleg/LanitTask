package ru.mloleg.lanittask.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mloleg.lanittask.dto.CreatePersonRequest;
import ru.mloleg.lanittask.dto.common.CommonRequest;
import ru.mloleg.lanittask.service.PeopleService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/person")
public class PeopleController {

    private final PeopleService peopleService;

    @PostMapping
    public void create(@RequestBody @Valid CommonRequest<CreatePersonRequest> request) {
        peopleService.createPerson(request.getBody());
    }
}
