package vnpt_it.vn.jobservice.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vnpt_it.vn.jobservice.domain.Job;
import vnpt_it.vn.jobservice.domain.res.ResJobDTO;
import vnpt_it.vn.jobservice.domain.res.RestResponse;
import vnpt_it.vn.jobservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.jobservice.exception.NotFoundException;
import vnpt_it.vn.jobservice.repository.JobSkillRepository;
import vnpt_it.vn.jobservice.service.JobService;
import vnpt_it.vn.jobservice.service.JobSkillService;
import vnpt_it.vn.jobservice.util.annotation.ApiMessage;

import java.util.List;

@RestController
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Create job")
    public ResponseEntity<ResJobDTO> createJob(@Valid @RequestBody Job job) {
        ResJobDTO resJobDTO = this.jobService.handleCreateJob(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(resJobDTO);
    }

    @PutMapping("/jobs")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Update job")
    public ResponseEntity<ResJobDTO> updateJob(@Valid @RequestBody Job job) throws NotFoundException {
        ResJobDTO resJobDTO = this.jobService.handleUpdateJob(job);
        return ResponseEntity.status(HttpStatus.OK).body(resJobDTO);
    }

    @DeleteMapping("/jobs/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Delete job")
    public ResponseEntity<?> deleteJob(@PathVariable long id) throws NotFoundException {
        this.jobService.handleDeleteJob(id);
        RestResponse<String> restResponse = new RestResponse<>();
        restResponse.setMessage("Delete job successfully");
        restResponse.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(restResponse);
    }

    @GetMapping("/jobs/skill/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','SCOPE_internal')")
    @ApiMessage("Get job by id")
    public ResponseEntity<List<ResJobDTO>> getJobsBySkillId(@PathVariable long id) throws NotFoundException {
        List<ResJobDTO> resJobDTOs = this.jobService.handleGetJobsBySkillId(id);
        return ResponseEntity.status(HttpStatus.OK).body(resJobDTOs);
    }
    @GetMapping("/jobs/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','SCOPE_internal')")
    @ApiMessage("Get job by id")
    public ResponseEntity<ResJobDTO> getJobById(@PathVariable long id) throws NotFoundException {
        ResJobDTO resJobDTO = this.jobService.handleGetJobById(id);
        return ResponseEntity.status(HttpStatus.OK).body(resJobDTO);
    }

    @GetMapping("/jobs")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','SCOPE_internal')")
    @ApiMessage("Get all jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(Pageable pageable, @Filter Specification<Job> specification) {
        ResultPaginationDTO resultPaginationDTO = this.jobService.handleGetAllJobs(specification, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }
}
