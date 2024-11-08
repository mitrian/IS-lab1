package com.lab1.dao.repositories;

import com.lab1.dao.entities.Address;
import com.lab1.dao.entities.Coordinates;
import com.lab1.dao.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
    Optional<Organization> findByIdAndCreatedBy(int id, String username);
    boolean existsByCoordinates(Coordinates coordinates);
    boolean existsByOfficialAddress(Address address);
    boolean existsByPostalAddress(Address address);
    @Query("SELECT AVG(o.rating) FROM Organization o")
    Double findAverageRating();
    @Query("SELECT COUNT(o) FROM Organization o WHERE o.rating > :minRating")
    long countByRatingGreaterThan(@Param("minRating") int minRating);
    List<Organization> findByFullNameStartingWith(String prefix);
}
