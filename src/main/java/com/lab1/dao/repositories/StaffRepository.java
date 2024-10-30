package com.lab1.dao.repositories;

import com.lab1.dao.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    Optional<Staff> findByIdAndCreatedBy(long id, String username);

    @Modifying
    @Transactional
    @Query("UPDATE Staff s SET s.organization = NULL WHERE s.organization.id = :organizationId")
    void setOrganizationToNullByOrganizationId(@Param("organizationId") int organizationId);

    @Modifying
    @Transactional
    @Query("UPDATE Staff s SET s.organization.id = :organizationId WHERE s.id = :staffId")
    void updateOrganizationForStaff(@Param("staffId") Long staffId, @Param("organizationId") int organizationId);
}
