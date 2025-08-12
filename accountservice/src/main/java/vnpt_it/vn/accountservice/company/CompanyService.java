package vnpt_it.vn.accountservice.company;

import feign.FeignException;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vnpt_it.vn.accountservice.domain.res.RestResponse;

@FeignClient(name = "company-service", configuration = ClientCredentialFeignConfig.class)
public interface CompanyService {
    @GetMapping("/companies/{id}")
    RestResponse<CompanyDTO> getCompanyById(@PathVariable("id") long id);
}
