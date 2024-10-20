package ru.mloleg.lanittask.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import ru.mloleg.lanittask.util.validator.ValidPastDate;

@Builder
public record CreatePersonRequest(
        @NotNull(message = "ID человека не может быть null.")
        @Positive(message = "ID человека должен быть больше 0.")
        Long id,
        @NotEmpty(message = "Имя человека не может быть пустым.")
        String name,
        @NotNull(message = "Дата рождения человека не может быть null.")
        @ValidPastDate()
        String birthdate) {

}

