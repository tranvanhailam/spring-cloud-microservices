package vnpt_it.vn.jobservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vnpt_it.vn.jobservice.domain.JobSkill;

import java.util.List;

public interface JobSkillRepository extends JpaRepository<JobSkill,Long> {

    List<JobSkill> findByJobId(long jobId);

    List<JobSkill> findBySkillId(long skillId);

    void deleteBySkillId(long skillId);

    void deleteByJobId(long jobId);
}
