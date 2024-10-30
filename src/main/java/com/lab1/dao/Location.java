package com.lab1.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "locations")
@NoArgsConstructor
@AllArgsConstructor
public class Location extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long x;

    private Long y; //не null

    private Float z; //не null

    private String name; //длина не больше 915, мб null

}
