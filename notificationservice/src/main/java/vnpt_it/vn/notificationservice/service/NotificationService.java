package vnpt_it.vn.notificationservice.service;

import vnpt_it.vn.notificationservice.model.MessageDTO;

public interface NotificationService {
    void sendEmail(MessageDTO messageDTO);
}
