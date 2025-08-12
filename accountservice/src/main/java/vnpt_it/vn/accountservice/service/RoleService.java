package vnpt_it.vn.accountservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vnpt_it.vn.accountservice.domain.Role;
import vnpt_it.vn.accountservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.accountservice.exception.NotFoundException;

public interface RoleService {
    Role handleCreateRole(Role role);

    Role handleUpdateRole(Role role) throws NotFoundException;

    void handleDeleteRole(long id) throws NotFoundException;

    Role handleGetRoleById(long id) throws NotFoundException;
    ResultPaginationDTO handleGetAllRoles(Specification<Role> specification, Pageable pageable);

}
