package vnpt_it.vn.accountservice.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vnpt_it.vn.accountservice.auth.AuthService;
import vnpt_it.vn.accountservice.domain.Role;
import vnpt_it.vn.accountservice.domain.res.RestResponse;
import vnpt_it.vn.accountservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.accountservice.exception.NotFoundException;
import vnpt_it.vn.accountservice.service.RoleService;
import vnpt_it.vn.accountservice.util.annotation.ApiMessage;

@RestController
public class RoleController {
    private final AuthService authService;
    private final RoleService roleService;

    public RoleController(AuthService authService, RoleService roleService) {
        this.authService = authService;
        this.roleService = roleService;
    }

    @GetMapping("/current-user")
    public String getUserLogin() {
        return authService.getUserInfo().getSub();
    }

    @PostMapping("/roles")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Create role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) {
        Role roleCreated = this.roleService.handleCreateRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleCreated);
    }

    @PutMapping("/roles")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Update role")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws NotFoundException {
        Role roleUpdated = this.roleService.handleUpdateRole(role);
        return ResponseEntity.status(HttpStatus.OK).body(roleUpdated);
    }

    @DeleteMapping("/roles/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Delete role")
    public ResponseEntity<?> deleteRole(@PathVariable long id) throws NotFoundException {
        this.roleService.handleDeleteRole(id);
            RestResponse<String> restResponse = new RestResponse<>();
            restResponse.setStatusCode(HttpStatus.OK.value());
            restResponse.setMessage("Delete role successfully!");
            return ResponseEntity.status(HttpStatus.OK).body(restResponse);
    }

    @GetMapping("/roles/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_internal')")
    @ApiMessage("Get role by id")
    public ResponseEntity<Role> getRoleById(@PathVariable long id) throws NotFoundException {
        Role role = this.roleService.handleGetRoleById(id);
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

    @GetMapping("/roles")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_internal')")
    @ApiMessage("Get all roles")
    public ResponseEntity<ResultPaginationDTO> getAllRoles(Pageable pageable, @Filter Specification<Role> specification) {
        ResultPaginationDTO resultPaginationDTO = this.roleService.handleGetAllRoles(specification, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }
}
