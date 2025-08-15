package vnpt_it.vn.jobservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vnpt_it.vn.jobservice.domain.Job;
import vnpt_it.vn.jobservice.domain.res.ResJobDTO;
import vnpt_it.vn.jobservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.jobservice.exception.NotFoundException;

public interface JobService {
    ResJobDTO handleCreateJob(Job job);

    ResJobDTO handleUpdateJob(Job job) throws NotFoundException;

    void handleDeleteJob(long id) throws NotFoundException;

    ResJobDTO handleGetJobById(long id) throws NotFoundException;

    ResultPaginationDTO handleGetAllJobs(Specification<Job> specification, Pageable pageable);
}
