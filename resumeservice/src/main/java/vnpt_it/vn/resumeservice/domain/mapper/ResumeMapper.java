package vnpt_it.vn.resumeservice.domain.mapper;

import org.springframework.stereotype.Component;
import vnpt_it.vn.resumeservice.account.AccountDTO;
import vnpt_it.vn.resumeservice.domain.Resume;
import vnpt_it.vn.resumeservice.domain.res.ResResumeDTO;
import vnpt_it.vn.resumeservice.job.JobDTO;


@Component
public class ResumeMapper {
    public ResResumeDTO mapResumeToResResumeDTO(Resume resume, AccountDTO accountDTO, JobDTO jobDTO ) {
        ResResumeDTO resResumeDTO = new ResResumeDTO();
        resResumeDTO.setId(resume.getId());
        resResumeDTO.setEmail(resume.getEmail());
        resResumeDTO.setUrl(resume.getUrl());
        resResumeDTO.setStatus(resume.getStatus());
        resResumeDTO.setCreatedAt(resume.getCreatedAt());
        resResumeDTO.setUpdatedAt(resume.getUpdatedAt());
        resResumeDTO.setCreatedBy(resume.getCreatedBy());
        resResumeDTO.setUpdatedBy(resume.getUpdatedBy());

        if(accountDTO != null) {
            ResResumeDTO.Account account = new ResResumeDTO.Account();
            account.setId(accountDTO.getId());
            account.setEmail(accountDTO.getEmail());
            resResumeDTO.setAccount(account);
        }

        if(jobDTO != null) {
            ResResumeDTO.Job job = new ResResumeDTO.Job();
            job.setId(jobDTO.getId());
            job.setName(jobDTO.getName());
            resResumeDTO.setJob(job);
        }

        return  resResumeDTO;
    }
}
