package ru.mloleg.lanittask.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CreateCarRequest(
        @NotNull(message = "ID машины не может быть null.")
        @Positive(message = "ID машины должен быть больше 0.")
        Long id,
        @NotEmpty(message = "Марка машины не может быть пустой.")
        @Pattern(regexp = "^[A-Za-z]+-[A-Za-z0-9]+$", message = "Недопустимый формат марки машины.")
        String model,
        @Positive(message = "Мощность машины должна быть больше 0.")
        @NotNull(message = "Мощность машины не может быть null.")
        Integer horsepower,
        @NotNull(message = "ID владельца машины не может быть null.")
        Long ownerId) {

}
