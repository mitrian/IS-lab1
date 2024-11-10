package com.lab1.dao.repositories;

import com.lab1.dao.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByIdAndCreatedBy(Long id, String username);
    void deleteByIdAndCreatedBy(Long id, String username);
}
