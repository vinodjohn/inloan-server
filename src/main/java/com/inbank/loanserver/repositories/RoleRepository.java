package com.inbank.loanserver.repositories;

import com.inbank.loanserver.models.Role;
import com.inbank.loanserver.models.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository to handle Role related data queries
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
public interface RoleRepository extends PagingAndSortingRepository<Role, UUID>, JpaRepository<Role, UUID> {
    Optional<Role> findByRoleType(RoleType roleType);
}
