package com.lab1.dao.entities;

import com.lab1.dao.enums.OrganizationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@Table(name = "organizations")
@NoArgsConstructor
@AllArgsConstructor
public class Organization extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    private String name; //не null, не мб пустой

    @ManyToOne
    private Coordinates coordinates; //не мб null

    private ZonedDateTime creationDate; //не мб null, генерируется автоматически

    @ManyToOne
    private Address officialAddress; //не мб null

    private int annualTurnover; //значение дб больше 0

    private int employeesCount; //значение дб больше 0

    private int rating; //значение дб больше 0

    private String fullName; //строка не мб пустой, длина строки не дб больше 902, мб null

    @Enumerated(EnumType.STRING)
    private OrganizationType type; //мб null

    @ManyToOne(fetch = FetchType.EAGER)
    private Address postalAddress; //не мб null


}
