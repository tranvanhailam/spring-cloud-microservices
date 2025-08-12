package vnpt_it.vn.accountservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnpt_it.vn.accountservice.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Page<Role> findAll(Specification<Role> specification, Pageable pageable);
}
