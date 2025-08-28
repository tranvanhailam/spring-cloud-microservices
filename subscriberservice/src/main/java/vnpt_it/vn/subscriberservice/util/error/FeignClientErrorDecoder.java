package vnpt_it.vn.subscriberservice.util.error;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;
import vnpt_it.vn.subscriberservice.exception.NotFoundException;

@Component
public class FeignClientErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new Default();
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 404 && methodKey.startsWith("AccountService#getAccountById")) {
            return new NotFoundException("Account not found");
        }
        if (response.status() == 404 && methodKey.startsWith("SkillService#getSkillById")) {
            return new NotFoundException("Skill not found");
        }
        if (response.status() == 404 && methodKey.startsWith("JobService#getJobById")) {
            return new NotFoundException("Job not found");
        }
        return defaultDecoder.decode(methodKey, response);
    }
}

