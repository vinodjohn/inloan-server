package com.inbank.loanserver.services;

import com.inbank.loanserver.exceptions.RoleNotFoundException;
import com.inbank.loanserver.models.Role;
import com.inbank.loanserver.models.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

/**
 * Service to handle Role related operations
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
public interface RoleService {
    /**
     * To create a new role
     *
     * @param role Role
     * @return Role
     */
    Role createRole(Role role);

    /**
     * To find a Role by ID
     *
     * @param id ID of a Role
     * @return Role
     */
    Role findRoleById(UUID id) throws RoleNotFoundException;

    /**
     * To find role by RoleType
     *
     * @param roleType Roletype
     * @return Role object
     */
    Role findRoleByRoleType(RoleType roleType) throws RoleNotFoundException;

    /**
     * To find all roles
     *
     * @param pageable Pageable of Roles
     * @return page of role
     */
    Page<Role> findAllRoles(Pageable pageable);

    /**
     * To update an existing role
     *
     * @param role Role
     * @return Role
     */
    Role updateRole(Role role) throws RoleNotFoundException;

    /**
     * To delete a role by ID
     *
     * @param id Role ID
     */
    void deleteRoleById(UUID id) throws RoleNotFoundException;

    /**
     * To restore a role by ID
     *
     * @param id Role ID
     */
    void restoreRoleById(UUID id) throws RoleNotFoundException;
}
