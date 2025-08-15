package vnpt_it.vn.jobservice.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
public class JobController {
    private final JobService jobService;
    private final JobSkillService jobSkillService;

    public JobController(JobService jobService, JobSkillService jobSkillService) {
        this.jobService = jobService;
        this.jobSkillService = jobSkillService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create job")
    public ResponseEntity<ResJobDTO> createJob(@Valid @RequestBody Job job) {
        ResJobDTO resJobDTO = this.jobService.handleCreateJob(job);
        return ResponseEntity.status(HttpStatus.CREATED).body(resJobDTO);
    }

    @PutMapping("/jobs")
    @ApiMessage("Update job")
    public ResponseEntity<ResJobDTO> updateJob(@Valid @RequestBody Job job) throws NotFoundException {
        ResJobDTO resJobDTO = this.jobService.handleUpdateJob(job);
//        ResJobDTO resJobDTO = new ResJobDTO();
//        this.jobSkillService.handleDeleteJobSkillByJobId(9);
        return ResponseEntity.status(HttpStatus.OK).body(resJobDTO);
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete job")
    public ResponseEntity<?> deleteJob(@PathVariable long id) throws NotFoundException {
        this.jobService.handleDeleteJob(id);
        RestResponse<String> restResponse = new RestResponse<>();
        restResponse.setMessage("Delete job successfully");
        restResponse.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(restResponse);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Get job by id")
    public ResponseEntity<ResJobDTO> getJobById(@PathVariable long id) throws NotFoundException {
        ResJobDTO resJobDTO = this.jobService.handleGetJobById(id);
        return ResponseEntity.status(HttpStatus.OK).body(resJobDTO);
    }

    @GetMapping("/jobs")
    @ApiMessage("Get all jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJobs(Pageable pageable, @Filter Specification<Job> specification) {
        ResultPaginationDTO resultPaginationDTO = this.jobService.handleGetAllJobs(specification, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }
}
