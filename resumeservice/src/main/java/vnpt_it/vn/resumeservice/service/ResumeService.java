package vnpt_it.vn.resumeservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vnpt_it.vn.resumeservice.domain.Resume;
import vnpt_it.vn.resumeservice.domain.res.ResResumeDTO;
import vnpt_it.vn.resumeservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.resumeservice.exception.NotFoundException;

public interface ResumeService {
    ResResumeDTO handleCreateResume(Resume resume);
    ResResumeDTO handleUpdateResume(Resume resume) throws NotFoundException;
    void handleDeleteResume(long id) throws NotFoundException;
    ResResumeDTO handleGetResumeById(long id) throws NotFoundException;
    ResultPaginationDTO  handleGetAllResumes(Specification<Resume> specification, Pageable pageable);
}
