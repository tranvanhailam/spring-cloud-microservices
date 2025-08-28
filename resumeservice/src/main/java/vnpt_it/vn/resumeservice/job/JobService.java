package vnpt_it.vn.resumeservice.job;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vnpt_it.vn.resumeservice.domain.res.RestResponse;

@FeignClient(name = "job-service", configuration = ClientCredentialFeignConfig.class)
public interface JobService {
    @GetMapping("/jobs/{id}")
    RestResponse<JobDTO> getJobById(@PathVariable("id") long id);
}
