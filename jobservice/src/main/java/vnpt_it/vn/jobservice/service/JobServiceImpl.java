package vnpt_it.vn.jobservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vnpt_it.vn.jobservice.auth.AuthService;
import vnpt_it.vn.jobservice.company.CompanyDTO;
import vnpt_it.vn.jobservice.company.CompanyService;
import vnpt_it.vn.jobservice.domain.Job;
import vnpt_it.vn.jobservice.domain.JobSkill;
import vnpt_it.vn.jobservice.domain.mapper.JobMapper;
import vnpt_it.vn.jobservice.domain.res.ResJobDTO;
import vnpt_it.vn.jobservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.jobservice.exception.NotFoundException;
import vnpt_it.vn.jobservice.repository.JobRepository;
import vnpt_it.vn.jobservice.skill.SkillDTO;
import vnpt_it.vn.jobservice.skill.SkillService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final AuthService authService;
    private final CompanyService companyService;
    private final JobMapper jobMapper;
    private final SkillService skillService;
    private final JobSkillService jobSkillService;

    public JobServiceImpl(JobRepository jobRepository, AuthService authService, CompanyService companyService, JobMapper jobMapper, SkillService skillService, JobSkillService jobSkillService) {
        this.jobRepository = jobRepository;
        this.authService = authService;
        this.companyService = companyService;
        this.jobMapper = jobMapper;
        this.skillService = skillService;
        this.jobSkillService = jobSkillService;
    }


    @Override
    public ResJobDTO handleCreateJob(Job job) {
        job.setCreatedBy(this.authService.getUserInfo().getSub());
        CompanyDTO companyDTO = null;
        if (job.getCompanyId() != 0) {
            companyDTO = this.companyService.getCompanyById(job.getCompanyId()).getData();
        }
        //Create job
        Job jobCreated = this.jobRepository.save(job);
        //Create JobSkill
        List<SkillDTO> skillDTOs = new ArrayList<>();
        if (job.getSkills() != null) {
            job.getSkills().forEach(skill -> {
                SkillDTO skillDTO = this.skillService.getSkillById(skill.getId()).getData();
                skillDTOs.add(skillDTO);
                JobSkill jobSkill = new JobSkill();
                jobSkill.setJobId(jobCreated.getId());
                jobSkill.setSkillId(skill.getId());
                this.jobSkillService.handleCreateJobSkill(jobSkill);
            });
        }
        return this.jobMapper.mapJobToResJobDTO(jobCreated, companyDTO, skillDTOs);
    }

    @Override
    public ResJobDTO handleUpdateJob(Job job) throws NotFoundException {
        Optional<Job> optionalJob = this.jobRepository.findById(job.getId());
        if (!optionalJob.isPresent()) {
            throw new NotFoundException("Job with id " + job.getId() + " not found");
        }
        Job jobToUpdate = optionalJob.get();
        jobToUpdate.setName(job.getName());
        jobToUpdate.setLocation(job.getLocation());
        jobToUpdate.setSalary(job.getSalary());
        jobToUpdate.setQuantity(job.getQuantity());
        jobToUpdate.setLevel(job.getLevel());
        jobToUpdate.setDescription(job.getDescription());
        jobToUpdate.setStartDate(job.getStartDate());
        jobToUpdate.setEndDate(job.getEndDate());
        jobToUpdate.setActive(job.isActive());
        jobToUpdate.setUpdatedBy(this.authService.getUserInfo().getSub());
        CompanyDTO companyDTO = null;
        if (job.getCompanyId() != 0) {
            companyDTO = this.companyService.getCompanyById(job.getCompanyId()).getData();
            jobToUpdate.setCompanyId(job.getCompanyId());
        } else jobToUpdate.setCompanyId(0);
        //Update job
        Job jobUpdated = this.jobRepository.save(jobToUpdate);
        //Update JobSkill
        List<SkillDTO> skillDTOs = new ArrayList<>();
        //Delete old JobSkill
        this.jobSkillService.handleDeleteJobSkillByJobId(jobUpdated.getId());
        if (job.getSkills() != null) {
            //Create new JobSkill
            job.getSkills().forEach(skill -> {
                SkillDTO skillDTO = this.skillService.getSkillById(skill.getId()).getData();
                skillDTOs.add(skillDTO);
                JobSkill jobSkill = new JobSkill();
                jobSkill.setJobId(jobUpdated.getId());
                jobSkill.setSkillId(skill.getId());
                this.jobSkillService.handleCreateJobSkill(jobSkill);
            });

        } //else this.jobSkillService.handleDeleteJobSkillByJobId(jobUpdated.getId());
        return this.jobMapper.mapJobToResJobDTO(jobUpdated, companyDTO, skillDTOs);
    }

    @Override
    public void handleDeleteJob(long id) throws NotFoundException {
        Optional<Job> optionalJob = this.jobRepository.findById(id);
        if (!optionalJob.isPresent()) {
            throw new NotFoundException("Job with id " + id + " not found");
        }
        //Delete Job
        this.jobRepository.deleteById(id);
        //Delete JobSkill
        this.jobSkillService.handleDeleteJobSkillByJobId(id);
    }

    @Override
    public ResJobDTO handleGetJobById(long id) throws NotFoundException {
        Optional<Job> optionalJob = this.jobRepository.findById(id);
        if (!optionalJob.isPresent()) {
            throw new NotFoundException("Job with id " + id + " not found");
        }
        Job job = optionalJob.get();
        //Get company
        CompanyDTO companyDTO = null;
        if (job.getCompanyId() != 0) {
            companyDTO = this.companyService.getCompanyById(job.getCompanyId()).getData();
        }
        //Get Skill list
        List<SkillDTO> skillDTOs = new ArrayList<>();
        List<JobSkill> jobSkills = this.jobSkillService.handleGetByJobId(job.getId());
        jobSkills.forEach(jobSkill -> {
            SkillDTO skillDTO = this.skillService.getSkillById(jobSkill.getSkillId()).getData();
            skillDTOs.add(skillDTO);
        });
        return this.jobMapper.mapJobToResJobDTO(job, companyDTO, skillDTOs);
    }

    @Override
        public List<ResJobDTO> handleGetJobsBySkillId(long id) {
        List<JobSkill> jobSkills = this.jobSkillService.handleGetBySkillId(id);
        List<ResJobDTO> jobDTOs = new ArrayList<>();
        jobSkills.forEach(jobSkill -> {
            try {
                ResJobDTO  resJobDTO = handleGetJobById(jobSkill.getJobId());
                jobDTOs.add(resJobDTO);
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        });
        return jobDTOs;
    }

    @Override
    public ResultPaginationDTO handleGetAllJobs(Specification<Job> specification, Pageable pageable) {
        Page<Job> jobPage = this.jobRepository.findAll(specification, pageable);

        List<ResJobDTO> resJobDTOs = jobPage.getContent().stream()
                .map(job -> {
                    ResJobDTO resJobDTO = new ResJobDTO();
                    resJobDTO.setId(job.getId());
                    resJobDTO.setName(job.getName());
                    resJobDTO.setLocation(job.getLocation());
                    resJobDTO.setSalary(job.getSalary());
                    resJobDTO.setQuantity(job.getQuantity());
                    resJobDTO.setLevel(job.getLevel());
                    resJobDTO.setDescription(job.getDescription());
                    resJobDTO.setStartDate(job.getStartDate());
                    resJobDTO.setEndDate(job.getEndDate());
                    resJobDTO.setActive(job.isActive());
                    resJobDTO.setCreatedAt(job.getCreatedAt());
                    resJobDTO.setUpdatedAt(job.getUpdatedAt());
                    resJobDTO.setCreatedBy(job.getCreatedBy());
                    resJobDTO.setUpdatedBy(job.getUpdatedBy());
                    //Get company
                    CompanyDTO companyDTO = null;
                    if (job.getCompanyId() != 0) {
                        companyDTO = this.companyService.getCompanyById(job.getCompanyId()).getData();
                        ResJobDTO.Company company = new ResJobDTO.Company();
                        company.setId(companyDTO.getId());
                        company.setName(companyDTO.getName());
                        resJobDTO.setCompany(company);
                    }
                    //Get Skill list
                    List<JobSkill> jobSkills = this.jobSkillService.handleGetByJobId(job.getId());
                    List<ResJobDTO.Skill> skills = new ArrayList<>();
                    jobSkills.forEach(jobSkill -> {
                        SkillDTO skillDTO = this.skillService.getSkillById(jobSkill.getSkillId()).getData();
                        ResJobDTO.Skill skill = new ResJobDTO.Skill();
                        skill.setId(skillDTO.getId());
                        skill.setName(skillDTO.getName());
                        skills.add(skill);
                    });
                    resJobDTO.setSkills(skills);
                    return resJobDTO;
                }).collect(Collectors.toList());

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPageNumber(jobPage.getNumber() + 1);
        meta.setPageSize(jobPage.getSize());
        meta.setTotalPages(jobPage.getTotalPages());
        meta.setTotalElements(jobPage.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(resJobDTOs);
        return resultPaginationDTO;
    }
}
