package vnpt_it.vn.jobservice.service;

import vnpt_it.vn.jobservice.domain.JobSkill;
import vnpt_it.vn.jobservice.exception.ExistsException;
import vnpt_it.vn.jobservice.exception.NotFoundException;

import java.util.List;

public interface JobSkillService {
    JobSkill handleCreateJobSkill(JobSkill jobSkill) ;

    void handleDeleteJobSkillBySkillId(long skillId);

    void handleDeleteJobSkillByJobId(long jobId);

    List<JobSkill> handleGetByJobId(long jobId);

    List<JobSkill> handleGetBySkillId(long skillId);
}
