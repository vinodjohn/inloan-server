package com.inbank.loanserver.services.implementations;

import com.inbank.loanserver.exceptions.RoleNotFoundException;
import com.inbank.loanserver.models.Role;
import com.inbank.loanserver.repositories.RoleRepository;
import com.inbank.loanserver.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementation of RoleService
 *
 * @author vinodjohn
 * @created 02.09.2024
 */
@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role createRole(Role role) {
        role.setName(role.getName().toUpperCase());
        role.setActive(true);
        return roleRepository.saveAndFlush(role);
    }

    @Override
    public Role findRoleById(UUID id) throws RoleNotFoundException {
        Optional<Role> role = roleRepository.findById(id);

        if (role.isEmpty()) {
            throw new RoleNotFoundException(id);
        }

        return role.get();
    }

    @Override
    public Role findRoleByName(String name) throws RoleNotFoundException {
        Optional<Role> role = roleRepository.findByName(name);

        if (role.isEmpty()) {
            throw new RoleNotFoundException(name);
        }

        return role.get();
    }


    @Override
    public Page<Role> findAllRoles(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public Role updateRole(Role role) throws RoleNotFoundException {
        if (findRoleById(role.getId()) != null) {
            role.setName(role.getName().toUpperCase());
            return roleRepository.saveAndFlush(role);
        }

        return null;
    }

    @Override
    public void deleteRoleById(UUID id) throws RoleNotFoundException {
        Role role = findRoleById(id);
        role.setActive(false);
        roleRepository.saveAndFlush(role);
    }

    @Override
    public void restoreRoleById(UUID id) throws RoleNotFoundException {
        Role role = findRoleById(id);
        role.setActive(true);
        roleRepository.saveAndFlush(role);
    }
}
