package vnpt_it.vn.subscriberservice.job;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vnpt_it.vn.subscriberservice.domain.res.RestResponse;
import vnpt_it.vn.subscriberservice.skill.SkillDTO;

import java.util.List;

@FeignClient(name = "job-service", configuration = ClientCredentialFeignConfig.class)
public interface JobService {
    @GetMapping("/jobs/{id}")
    RestResponse<JobDTO> getJobById(@PathVariable("id") long id);
    @GetMapping("/jobs/skill/{id}")
    RestResponse<List<JobDTO>> getJobsBySkillId(@PathVariable("id") long id);
}
