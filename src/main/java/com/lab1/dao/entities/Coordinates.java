package com.lab1.dao.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "coordinates")
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive(message = "Больше 0")
    private Double x; //Максимальное значение 484; Поле не может быть null

    private float y;
}
