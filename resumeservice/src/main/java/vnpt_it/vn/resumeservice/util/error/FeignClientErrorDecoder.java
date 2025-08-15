package vnpt_it.vn.resumeservice.util.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;
import vnpt_it.vn.resumeservice.exception.NotFoundException;

@Component
public class FeignClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404 && methodKey.startsWith("CompanyService#getCompanyById")) {
            return new NotFoundException("Company not found");
        }
        if (response.status() == 404 && methodKey.startsWith("SkillService#getSkillById")) {
            return new NotFoundException("Skill not found");
        }
        return defaultDecoder.decode(methodKey, response);
    }
}

