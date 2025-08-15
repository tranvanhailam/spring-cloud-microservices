package vnpt_it.vn.jobservice.domain.mapper;

import org.springframework.stereotype.Component;
import vnpt_it.vn.jobservice.company.CompanyDTO;
import vnpt_it.vn.jobservice.domain.Job;
import vnpt_it.vn.jobservice.domain.res.ResJobDTO;
import vnpt_it.vn.jobservice.skill.SkillDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class JobMapper {
    public ResJobDTO mapJobToResJobDTO(Job job, CompanyDTO companyDTO, List<SkillDTO> skillDTOS) {
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

        if (companyDTO != null) {
            ResJobDTO.Company company = new ResJobDTO.Company();
            company.setId(companyDTO.getId());
            company.setName(companyDTO.getName());
            resJobDTO.setCompany(company);
        }

        if (!skillDTOS.isEmpty()) {
            List<ResJobDTO.Skill> skills = new ArrayList<>();
            skillDTOS.forEach(skillDTO -> {
                ResJobDTO.Skill skill = new ResJobDTO.Skill();
                skill.setId(skillDTO.getId());
                skill.setName(skillDTO.getName());
                skills.add(skill);
            });
            resJobDTO.setSkills(skills);
        }

        return resJobDTO;
    }
}
