package ru.mloleg.lanittask.util.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PastDateValidator.class)
public @interface ValidPastDate {

    String message() default "Дата должна быть в прошлом и соответствовать формату \"дд.ММ.гггг\".";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
