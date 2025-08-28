package vnpt_it.vn.notificationservice.service;

import jakarta.mail.MessagingException;
import vnpt_it.vn.notificationservice.domain.MessageDTO;
import vnpt_it.vn.notificationservice.domain.MessageJobIntroductionDTO;

public interface NotificationService {
    void sendEmail(MessageDTO messageDTO);
    void sendEmailJobIntroduction(MessageJobIntroductionDTO messageJobIntroductionDTO) throws MessagingException;
}
