package vnpt_it.vn.companyservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vnpt_it.vn.companyservice.auth.AuthService;
import vnpt_it.vn.companyservice.domain.Company;
import vnpt_it.vn.companyservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.companyservice.exception.NotFoundException;
import vnpt_it.vn.companyservice.repository.CompanyRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final CompanyRepository companyRepository;
    private final AuthService authService;

    public CompanyServiceImpl(CompanyRepository companyRepository, AuthService authService) {
        this.companyRepository = companyRepository;
        this.authService = authService;
    }

    @Override
    public Company handleCreateCompany(Company company) {
        company.setCreatedBy(this.authService.getUserInfo().getSub());
        return this.companyRepository.save(company);
    }

    @Override
    public Company handleUpdateCompany(Company company) throws NotFoundException {
        Optional<Company> optionalCompany = this.companyRepository.findById(company.getId());
        if (optionalCompany.isPresent()) {
            Company companyToUpdate = optionalCompany.get();
//            companyToUpdate.setId(company.getId());
            companyToUpdate.setName(company.getName());
            companyToUpdate.setAddress(company.getAddress());
            companyToUpdate.setDescription(company.getDescription());
            companyToUpdate.setLogo(company.getLogo());
            companyToUpdate.setUpdatedBy(this.authService.getUserInfo().getSub());
            return this.companyRepository.save(companyToUpdate);
        }
        throw new NotFoundException("Company with id " + company.getId() + " not found");
    }

    @Override
    public void handleDeleteCompany(long id) throws NotFoundException {
        Optional<Company> optionalCompany = this.companyRepository.findById(id);
        if (!optionalCompany.isPresent()) {
            throw new NotFoundException("Company with id " + id + " not found");
        }
        this.companyRepository.delete(optionalCompany.get());
    }

    @Override
    public Company handleGetCompanyById(long id) throws NotFoundException {
        Optional<Company> optionalCompany = this.companyRepository.findById(id);
        if (optionalCompany.isPresent()) {
            return optionalCompany.get();
        }
        throw new NotFoundException("Company with id " + id + " not found");
    }

    @Override
    public ResultPaginationDTO handleGetAllCompanies(Specification<Company> specification, Pageable pageable) {
        Page<Company> companyPage = this.companyRepository.findAll(specification, pageable);

        List<Company> companies = companyPage.getContent().stream()
                .map(company -> {
                    Company companyShow = new Company();
                    companyShow.setId(company.getId());
                    companyShow.setName(company.getName());
                    companyShow.setAddress(company.getAddress());
                    companyShow.setDescription(company.getDescription());
                    companyShow.setLogo(company.getLogo());
                    companyShow.setCreatedAt(company.getCreatedAt());
                    companyShow.setUpdatedAt(company.getUpdatedAt());
                    companyShow.setCreatedBy(company.getCreatedBy());
                    companyShow.setUpdatedBy(company.getUpdatedBy());
                    return companyShow;
                }).collect(Collectors.toList());

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPageNumber(companyPage.getNumber() + 1);
        meta.setPageSize(companyPage.getSize());
        meta.setTotalPages(companyPage.getTotalPages());
        meta.setTotalElements(companyPage.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(companies);
        return resultPaginationDTO;
    }


}
