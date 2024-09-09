package com.inbank.loanserver.controllers;

import com.inbank.loanserver.dtos.ObjectListDto;
import com.inbank.loanserver.exceptions.RoleNotFoundException;
import com.inbank.loanserver.models.Role;
import com.inbank.loanserver.services.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.inbank.loanserver.utils.Constants.Data.DEFAULT_ITEMS_PER_PAGE;
import static com.inbank.loanserver.utils.LoanUtils.getSortOfColumn;

/**
 * Controller to handle Role related requests
 *
 * @author vinodjohn
 * @created 09.09.2024
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseEntity<ObjectListDto> getSortedRoleByPage(
            @RequestParam(name = "page", defaultValue = "0") int pageNum,
            @RequestParam(name = "items", defaultValue = DEFAULT_ITEMS_PER_PAGE) int totalItem,
            @RequestParam(name = "sort", defaultValue = "createdDate") String sort,
            @RequestParam(name = "order", defaultValue = "desc") String order) {
        Page<Role> rolePage = roleService.findAllRoles(PageRequest.of(pageNum, totalItem,
                getSortOfColumn(sort, order)));
        List<Role> roleList = rolePage.stream()
                .collect(Collectors.toList());
        ObjectListDto objectListDto = new ObjectListDto(roleList, pageNum, rolePage.getTotalElements());

        return ResponseEntity.ok(objectListDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleById(@PathVariable UUID id) throws RoleNotFoundException {
        Role role = roleService.findRoleById(id);
        return ResponseEntity.ok(role);
    }

    @PostMapping
    public ResponseEntity<?> createRole(@Valid @RequestBody Role role) {
        Role newRole = roleService.createRole(role);
        return new ResponseEntity<>(newRole, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateRole(@Valid @RequestBody Role role) throws RoleNotFoundException {
        Role updatedRole = roleService.updateRole(role);
        return ResponseEntity.ok(updatedRole);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable UUID id) throws RoleNotFoundException {
        roleService.deleteRoleById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restoreRole(@PathVariable UUID id) throws RoleNotFoundException {
        roleService.restoreRoleById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

