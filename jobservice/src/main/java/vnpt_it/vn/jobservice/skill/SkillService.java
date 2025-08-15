package vnpt_it.vn.jobservice.skill;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vnpt_it.vn.jobservice.company.CompanyDTO;
import vnpt_it.vn.jobservice.domain.res.RestResponse;

@FeignClient(name = "skill-service", configuration = ClientCredentialFeignConfig.class)
public interface SkillService {
    @GetMapping("/skills/{id}")
    RestResponse<SkillDTO> getSkillById(@PathVariable("id") long id);
}
