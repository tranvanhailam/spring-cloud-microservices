package vnpt_it.vn.companyservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnpt_it.vn.companyservice.domain.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {
    Page<Company> findAll(Specification<Company> specification, Pageable pageable);
}
