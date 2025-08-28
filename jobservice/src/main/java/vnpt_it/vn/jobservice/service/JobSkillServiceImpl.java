package vnpt_it.vn.jobservice.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vnpt_it.vn.jobservice.auth.AuthService;
import vnpt_it.vn.jobservice.domain.JobSkill;
import vnpt_it.vn.jobservice.exception.ExistsException;
import vnpt_it.vn.jobservice.exception.NotFoundException;
import vnpt_it.vn.jobservice.repository.JobSkillRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class JobSkillServiceImpl implements JobSkillService {
    private final JobSkillRepository jobSkillRepository;
    private final AuthService authService;

    public JobSkillServiceImpl(JobSkillRepository jobSkillRepository, AuthService authService) {
        this.jobSkillRepository = jobSkillRepository;
        this.authService = authService;
    }

    @Override
    public JobSkill handleCreateJobSkill(JobSkill jobSkill)  {
        jobSkill.setCreatedBy(this.authService.getUserInfo().getSub());
        return this.jobSkillRepository.save(jobSkill);
    }

    @Override
    public void handleDeleteJobSkillBySkillId(long skillId) {
        this.jobSkillRepository.deleteBySkillId(skillId);
        this.jobSkillRepository.flush();
    }

    @Override
    public void handleDeleteJobSkillByJobId(long jobId) {
        this.jobSkillRepository.deleteByJobId(jobId);
        this.jobSkillRepository.flush();
    }

    @Override
    public List<JobSkill> handleGetByJobId(long jobId) {
        return this.jobSkillRepository.findByJobId(jobId);
    }

    @Override
    public List<JobSkill> handleGetBySkillId(long skillId) {
        return this.jobSkillRepository.findBySkillId(skillId);
    }
}
