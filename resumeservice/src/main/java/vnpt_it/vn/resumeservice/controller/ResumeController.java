package vnpt_it.vn.resumeservice.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vnpt_it.vn.resumeservice.domain.Resume;
import vnpt_it.vn.resumeservice.domain.res.ResResumeDTO;
import vnpt_it.vn.resumeservice.domain.res.RestResponse;
import vnpt_it.vn.resumeservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.resumeservice.exception.NotFoundException;
import vnpt_it.vn.resumeservice.service.ResumeService;
import vnpt_it.vn.resumeservice.util.annotation.ApiMessage;

@RestController
public class ResumeController {
    private final ResumeService resumeService;

    public  ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Create resume")
    public ResponseEntity<ResResumeDTO> createResume(@Valid @RequestBody Resume resume) {
        ResResumeDTO resResumeDTO= this.resumeService.handleCreateResume(resume);
        return ResponseEntity.status(HttpStatus.CREATED).body(resResumeDTO);
    }

    @PutMapping("/resumes")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Update resume")
    public ResponseEntity<ResResumeDTO> updateResume(@Valid @RequestBody Resume resume) throws NotFoundException {
        ResResumeDTO resResumeDTO= this.resumeService.handleUpdateResume(resume);
        return ResponseEntity.status(HttpStatus.OK).body(resResumeDTO);
    }

    @DeleteMapping("/resumes/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Delete resumes")
    public ResponseEntity<?> deleteResume(@PathVariable long id) throws NotFoundException {
        this.resumeService.handleDeleteResume(id);
        RestResponse<String> restResponse= new RestResponse<>();
        restResponse.setMessage("Delete resume successfully!");
        restResponse.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(restResponse);
    }

    @GetMapping("/resumes/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_internal')")
    @ApiMessage("Get resume by id")
    public ResponseEntity<ResResumeDTO> getResumeById(@PathVariable long id) throws NotFoundException {
        ResResumeDTO resResumeDTO= this.resumeService.handleGetResumeById(id);
        return ResponseEntity.status(HttpStatus.OK).body(resResumeDTO);
    }

    @GetMapping("/resumes")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_internal')")
    @ApiMessage("Get all resumes")
    public ResponseEntity<ResultPaginationDTO>  getAllResumes(Pageable pageable,@Filter Specification<Resume> specification) {
        ResultPaginationDTO resultPaginationDTO= this.resumeService.handleGetAllResumes(specification, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }


}
