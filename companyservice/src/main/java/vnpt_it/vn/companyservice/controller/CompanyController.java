package vnpt_it.vn.companyservice.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vnpt_it.vn.companyservice.auth.AuthService;
import vnpt_it.vn.companyservice.domain.Company;
import vnpt_it.vn.companyservice.domain.res.RestResponse;
import vnpt_it.vn.companyservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.companyservice.exception.NotFoundException;
import vnpt_it.vn.companyservice.service.CompanyService;
import vnpt_it.vn.companyservice.util.annotation.ApiMessage;
import vnpt_it.vn.companyservice.util.annotation.ValidationCreateCompany;
import vnpt_it.vn.companyservice.util.annotation.ValidationUpdateCompany;

@RestController
public class CompanyController {
    private final AuthService authService;
    private final CompanyService companyService;

    public CompanyController(AuthService authService, CompanyService companyService) {
        this.authService = authService;
        this.companyService = companyService;
    }

    @GetMapping("/current-user")
    public String getUserLogin() {
        return authService.getUserInfo().getSub();
    }

    @PostMapping("/companies")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Create company")
    @ValidationCreateCompany
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company company) {
        Company companyCreated = this.companyService.handleCreateCompany(company);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyCreated);
    }

    @PutMapping("/companies")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Update company")
    @ValidationUpdateCompany
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) throws NotFoundException {
        Company companyUpdated = this.companyService.handleUpdateCompany(company);
        return ResponseEntity.status(HttpStatus.OK).body(companyUpdated);
    }

    @DeleteMapping("/companies/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Delete company")
    public ResponseEntity<?> deleteCompany(@PathVariable long id) throws NotFoundException {
        this.companyService.handleDeleteCompany(id);
        RestResponse<String> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.OK.value());
        restResponse.setMessage("Delete company successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(restResponse);
    }

    @GetMapping("/companies/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_internal')")
    @ApiMessage("Get company by id")
    public ResponseEntity<Company> getCompanyById(@PathVariable long id) throws NotFoundException {
        Company company = this.companyService.handleGetCompanyById(id);
        return ResponseEntity.status(HttpStatus.OK).body(company);
    }

    @GetMapping("/companies")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_internal')")
    @ApiMessage("Get all companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompanies(Pageable pageable, @Filter Specification<Company> specification) {
        ResultPaginationDTO resultPaginationDTO = this.companyService.handleGetAllCompanies(specification, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }
}
