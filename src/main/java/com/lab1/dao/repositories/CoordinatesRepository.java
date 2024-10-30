package com.lab1.dao.repositories;

import com.lab1.dao.entities.Coordinates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CoordinatesRepository extends JpaRepository<Coordinates, Long> {
}
