package vnpt_it.vn.subscriberservice.service;

import vnpt_it.vn.subscriberservice.domain.MessageJobIntroductionDTO;

import java.util.List;

public interface EmailService {
    public List<MessageJobIntroductionDTO> handleSendEmail();
}
