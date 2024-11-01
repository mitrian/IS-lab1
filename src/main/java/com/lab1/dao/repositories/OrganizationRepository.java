package com.lab1.dao.repositories;

import com.lab1.dao.entities.Coordinates;
import com.lab1.dao.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    boolean existsByCoordinates(Coordinates coordinates);
}
