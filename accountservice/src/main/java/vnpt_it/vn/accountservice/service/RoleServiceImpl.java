package vnpt_it.vn.accountservice.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vnpt_it.vn.accountservice.auth.AuthService;
import vnpt_it.vn.accountservice.domain.Role;
import vnpt_it.vn.accountservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.accountservice.exception.NotFoundException;
import vnpt_it.vn.accountservice.repository.RoleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final AuthService authService;

    public RoleServiceImpl(RoleRepository roleRepository, AuthService authService) {
        this.roleRepository = roleRepository;
        this.authService = authService;
    }

    @CachePut(value = "roles", key = "#role.id")
    @Override
    public Role handleCreateRole(Role role) {
        role.setCreatedBy(this.authService.getUserInfo().getSub());
        return this.roleRepository.save(role);
    }

    @CachePut(value = "roles", key = "#role.id")
    @Override
    public Role handleUpdateRole(Role role) throws NotFoundException {
        Optional<Role> optionalRole = this.roleRepository.findById(role.getId());
        if (optionalRole.isPresent()) {
            Role roleToUpdate = optionalRole.get();
            roleToUpdate.setId(role.getId());
            roleToUpdate.setName(role.getName());
            roleToUpdate.setDescription(role.getDescription());
            roleToUpdate.setActive(role.isActive());
            roleToUpdate.setUpdatedBy(this.authService.getUserInfo().getSub());
            return this.roleRepository.save(roleToUpdate);
        }
        throw new NotFoundException("Role with id " + role.getId() + " not found");
    }

    @CacheEvict(value = "roles", key = "#id")
    @Override
    public void handleDeleteRole(long id) throws NotFoundException {
        Optional<Role> optionalRole = this.roleRepository.findById(id);
        if (!optionalRole.isPresent()) {
            throw new NotFoundException("Role with id " + id + " not found");
        }
        this.roleRepository.delete(optionalRole.get());
    }

    @Cacheable(value = "roles", key = "#id")
    @Override
    public Role handleGetRoleById(long id) throws NotFoundException {
        Optional<Role> optionalRole = this.roleRepository.findById(id);
        if (optionalRole.isPresent()) {
            return optionalRole.get();
        }
        throw new NotFoundException("Role with id " + id + " not found");
    }

    @Override
    public ResultPaginationDTO handleGetAllRoles(Specification<Role> specification, Pageable pageable) {
        Page<Role> rolePage = this.roleRepository.findAll(specification, pageable);

        List<Role> roles = rolePage.getContent().stream()
                .map(role -> {
                    Role roleShow = new Role();
                    roleShow.setId(role.getId());
                    roleShow.setName(role.getName());
                    roleShow.setDescription(role.getDescription());
                    roleShow.setActive(role.isActive());
                    roleShow.setCreatedAt(role.getCreatedAt());
                    roleShow.setUpdatedAt(role.getUpdatedAt());
                    roleShow.setCreatedBy(role.getCreatedBy());
                    roleShow.setUpdatedBy(role.getUpdatedBy());
                    return roleShow;
                }).collect(Collectors.toList());

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPageNumber(rolePage.getNumber() + 1);
        meta.setPageSize(rolePage.getSize());
        meta.setTotalPages(rolePage.getTotalPages());
        meta.setTotalElements(rolePage.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(roles);
        return resultPaginationDTO;
    }


}
