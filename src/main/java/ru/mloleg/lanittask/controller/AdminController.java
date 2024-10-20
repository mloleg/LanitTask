package ru.mloleg.lanittask.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mloleg.lanittask.dto.PersonWithCarsResponse;
import ru.mloleg.lanittask.dto.StatisticsResponse;
import ru.mloleg.lanittask.dto.common.CommonResponse;
import ru.mloleg.lanittask.service.AdminService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/personwithcars")
    public CommonResponse<PersonWithCarsResponse> personWithCars(
            @RequestParam("personid") @NotNull @Positive(message = "ID человека должен быть больше 0.") Long id) {
        return CommonResponse.<PersonWithCarsResponse>builder()
                .body(adminService.getPersonWithCars(id))
                .build();
    }

    @GetMapping("/statistics")
    public CommonResponse<StatisticsResponse> statistics() {
        return CommonResponse.<StatisticsResponse>builder()
                .body(adminService.getStatistics())
                .build();
    }

    @GetMapping("/clear")
    public void clear() {
        adminService.clear();
    }
}
