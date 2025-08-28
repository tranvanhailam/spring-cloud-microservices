package vnpt_it.vn.resumeservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vnpt_it.vn.resumeservice.account.AccountDTO;
import vnpt_it.vn.resumeservice.account.AccountService;
import vnpt_it.vn.resumeservice.auth.AuthService;
import vnpt_it.vn.resumeservice.domain.Resume;
import vnpt_it.vn.resumeservice.domain.mapper.ResumeMapper;
import vnpt_it.vn.resumeservice.domain.res.ResResumeDTO;
import vnpt_it.vn.resumeservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.resumeservice.exception.NotFoundException;
import vnpt_it.vn.resumeservice.job.JobDTO;
import vnpt_it.vn.resumeservice.job.JobService;
import vnpt_it.vn.resumeservice.repository.ResumeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository resumeRepository;
    private final AccountService accountService;
    private final JobService jobService;
    private final ResumeMapper resumeMapper;
    private final AuthService authService;
    public ResumeServiceImpl(ResumeRepository resumeRepository, AccountService accountService, JobService jobService, ResumeMapper resumeMapper, AuthService authService) {
        this.resumeRepository = resumeRepository;
        this.accountService = accountService;
        this.jobService = jobService;
        this.resumeMapper = resumeMapper;
        this.authService = authService;
    }
    @Override
    public ResResumeDTO handleCreateResume(Resume resume) {
        resume.setCreatedBy(this.authService.getUserInfo().getSub());
        AccountDTO accountDTO=null;
        if(resume.getAccountId()!=0){
            accountDTO= this.accountService.getAccountById(resume.getAccountId()).getData();
        }
        JobDTO jobDTO=null;
        if(resume.getJobId()!=0){
            jobDTO= this.jobService.getJobById(resume.getJobId()).getData();
        }
        return this.resumeMapper.mapResumeToResResumeDTO(this.resumeRepository.save(resume),accountDTO,jobDTO);
    }

    @Override
    public ResResumeDTO handleUpdateResume(Resume resume) throws NotFoundException {
        Optional<Resume> resumeOptional = this.resumeRepository.findById(resume.getId());
        if(!resumeOptional.isPresent()){
            throw new NotFoundException("Resume with id "+resume.getId()+" not found");
        }
        Resume resumeToUpdate = resumeOptional.get();
        resumeToUpdate.setEmail(resume.getEmail());
        resumeToUpdate.setUrl(resume.getUrl());
        resumeToUpdate.setStatus(resume.getStatus());
        resumeToUpdate.setUpdatedBy(this.authService.getUserInfo().getSub());

        AccountDTO accountDTO=null;
        if(resume.getAccountId()!=0){
            accountDTO= this.accountService.getAccountById(resume.getAccountId()).getData();
            resumeToUpdate.setAccountId(resume.getAccountId());
        } else resumeToUpdate.setAccountId(0);
        JobDTO jobDTO=null;
        if(resume.getJobId()!=0){
            jobDTO= this.jobService.getJobById(resume.getJobId()).getData();
            resumeToUpdate.setJobId(resume.getJobId());
        } else resumeToUpdate.setJobId(0);

        return this.resumeMapper.mapResumeToResResumeDTO(this.resumeRepository.save(resumeToUpdate),accountDTO,jobDTO);
    }

    @Override
    public void handleDeleteResume(long id) throws NotFoundException {
        Optional<Resume> resumeOptional = this.resumeRepository.findById(id);
        if(!resumeOptional.isPresent()){
            throw new NotFoundException("Resume with id "+id+" not found");
        }
        this.resumeRepository.deleteById(id);
    }

    @Override
    public ResResumeDTO handleGetResumeById(long id) throws NotFoundException {
        Optional<Resume> resumeOptional = this.resumeRepository.findById(id);
        if(!resumeOptional.isPresent()){
            throw new NotFoundException("Resume with id "+id+" not found");
        }
        Resume resume = resumeOptional.get();
        AccountDTO accountDTO=null;
        if(resume.getAccountId()!=0){
            accountDTO= this.accountService.getAccountById(resume.getAccountId()).getData();
        }
        JobDTO jobDTO=null;
        if(resume.getJobId()!=0){
            jobDTO= this.jobService.getJobById(resume.getJobId()).getData();
        }
        return this.resumeMapper.mapResumeToResResumeDTO(resume,accountDTO,jobDTO);
    }

    @Override
    public ResultPaginationDTO handleGetAllResumes(Specification<Resume> specification, Pageable pageable) {
        Page<Resume> resumePage= this.resumeRepository.findAll(specification,pageable);
        List<ResResumeDTO> resResumeDTOs= resumePage.getContent().stream()
                .map(resume -> {

                    ResResumeDTO resResumeDTO = new ResResumeDTO();
                    resResumeDTO.setId(resume.getId());
                    resResumeDTO.setEmail(resume.getEmail());
                    resResumeDTO.setUrl(resume.getUrl());
                    resResumeDTO.setStatus(resume.getStatus());
                    resResumeDTO.setCreatedAt(resume.getCreatedAt());
                    resResumeDTO.setUpdatedAt(resume.getUpdatedAt());
                    resResumeDTO.setCreatedBy(resume.getCreatedBy());
                    resResumeDTO.setUpdatedBy(resume.getUpdatedBy());
                    AccountDTO accountDTO=null;
                    if(resume.getAccountId()!=0){
                        accountDTO= this.accountService.getAccountById(resume.getAccountId()).getData();
                        ResResumeDTO.Account account = new ResResumeDTO.Account();
                        account.setId(accountDTO.getId());
                        account.setEmail(accountDTO.getEmail());
                        resResumeDTO.setAccount(account);
                    }
                    JobDTO jobDTO=null;
                    if(resume.getJobId()!=0){
                        jobDTO= this.jobService.getJobById(resume.getJobId()).getData();
                        ResResumeDTO.Job job = new ResResumeDTO.Job();
                        job.setId(jobDTO.getId());
                        job.setName(jobDTO.getName());
                        resResumeDTO.setJob(job);
                    }
                    return resResumeDTO;
                }).collect(Collectors.toList());
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPageNumber(resumePage.getNumber() + 1);
        meta.setPageSize(resumePage.getSize());
        meta.setTotalPages(resumePage.getTotalPages());
        meta.setTotalElements(resumePage.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(resResumeDTOs);
        return resultPaginationDTO;
    }
}
