package vnpt_it.vn.jobservice.company;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vnpt_it.vn.jobservice.company.ClientCredentialFeignConfig;
import vnpt_it.vn.jobservice.domain.res.RestResponse;

@FeignClient(name = "company-service", configuration = ClientCredentialFeignConfig.class)
public interface CompanyService {
    @GetMapping("/companies/{id}")
    RestResponse<CompanyDTO> getCompanyById(@PathVariable("id") long id);
}
