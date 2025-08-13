package vnpt_it.vn.jobservice.domain.mapper;

import org.springframework.stereotype.Component;
import vnpt_it.vn.jobservice.company.CompanyDTO;
import vnpt_it.vn.jobservice.domain.Job;
import vnpt_it.vn.jobservice.domain.res.ResJobDTO;

@Component
public class JobMapper {
    public ResJobDTO mapJobToResJobDTO(Job job, CompanyDTO companyDTO) {
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
        return resJobDTO;
    }
}
