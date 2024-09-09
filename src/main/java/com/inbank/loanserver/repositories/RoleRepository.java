package com.inbank.loanserver.repositories;

import com.inbank.loanserver.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository to handle Role related data queries
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, UUID>, JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);
}
