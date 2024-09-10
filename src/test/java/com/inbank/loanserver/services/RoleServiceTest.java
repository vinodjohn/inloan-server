package com.inbank.loanserver.services;

import com.inbank.loanserver.exceptions.RoleNotFoundException;
import com.inbank.loanserver.models.Role;
import com.inbank.loanserver.repositories.RoleRepository;
import com.inbank.loanserver.services.implementations.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Tests for RoleService
 *
 * @author vinodjohn
 * @created 10.09.2024
 */
@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    private static final UUID ROLE_ID = UUID.randomUUID();
    private static final String ROLE_NAME = "ADMIN";

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = createTestRole();
    }

    private Role createTestRole() {
        Role testRole = new Role();
        testRole.setId(ROLE_ID);
        testRole.setName(ROLE_NAME);
        testRole.setActive(true);
        return testRole;
    }

    @Test
    void testFindRoleByIdWhenValidIdThenRoleIsReturned() throws RoleNotFoundException {
        when(roleRepository.findById(ROLE_ID)).thenReturn(Optional.of(role));

        Role foundRole = roleService.findRoleById(ROLE_ID);

        assertEquals(role, foundRole);
        verify(roleRepository, times(1)).findById(ROLE_ID);
    }

    @Test
    void testFindRoleByIdWhenInvalidIdThenExceptionIsThrown() {
        when(roleRepository.findById(ROLE_ID)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.findRoleById(ROLE_ID));
        verify(roleRepository, times(1)).findById(ROLE_ID);
    }

    @Test
    void testFindRoleByNameWhenValidNameThenRoleIsReturned() throws RoleNotFoundException {
        when(roleRepository.findByName(ROLE_NAME)).thenReturn(Optional.of(role));

        Role foundRole = roleService.findRoleByName(ROLE_NAME);

        assertEquals(role, foundRole);
        verify(roleRepository, times(1)).findByName(ROLE_NAME);
    }

    @Test
    void testFindRoleByNameWhenInvalidNameThenExceptionIsThrown() {
        when(roleRepository.findByName(ROLE_NAME)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.findRoleByName(ROLE_NAME));
        verify(roleRepository, times(1)).findByName(ROLE_NAME);
    }

    @Test
    void testFindAllRolesWhenValidPageableThenPageIsReturned() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Role> rolePage = new PageImpl<>(List.of(role));
        when(roleRepository.findAll(pageable)).thenReturn(rolePage);

        Page<Role> foundRoles = roleService.findAllRoles(pageable);

        assertEquals(rolePage, foundRoles);
        verify(roleRepository, times(1)).findAll(pageable);
    }

    @Test
    void testRestoreRoleByIdWhenInvalidIdThenExceptionIsThrown() {
        when(roleRepository.findById(ROLE_ID)).thenReturn(Optional.empty());

        assertThrows(RoleNotFoundException.class, () -> roleService.restoreRoleById(ROLE_ID));
        verify(roleRepository, times(1)).findById(ROLE_ID);
    }
}