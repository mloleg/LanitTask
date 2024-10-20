package ru.mloleg.lanittask.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import ru.mloleg.lanittask.model.common.AuditableEntity;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person extends AuditableEntity {

    @Id
    private Long id;
    private String name;
    private LocalDate birthdate;

}
