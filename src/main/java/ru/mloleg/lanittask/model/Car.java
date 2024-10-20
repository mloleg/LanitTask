package ru.mloleg.lanittask.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import ru.mloleg.lanittask.model.common.AuditableEntity;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Car extends AuditableEntity {

    @Id
    private Long id;
    private String model;
    private Integer horsepower;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Person owner;

}
