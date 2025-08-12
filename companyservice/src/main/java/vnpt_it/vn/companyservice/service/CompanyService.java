package vnpt_it.vn.companyservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vnpt_it.vn.companyservice.domain.Company;
import vnpt_it.vn.companyservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.companyservice.exception.NotFoundException;

public interface CompanyService {
    Company handleCreateCompany(Company company);

    Company handleUpdateCompany(Company company) throws NotFoundException;

    void handleDeleteCompany(long id) throws NotFoundException;

    Company handleGetCompanyById(long id) throws NotFoundException;
    ResultPaginationDTO handleGetAllCompanies(Specification<Company> specification, Pageable pageable);

}
