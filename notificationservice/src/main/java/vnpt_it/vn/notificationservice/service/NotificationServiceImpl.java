package vnpt_it.vn.notificationservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import vnpt_it.vn.notificationservice.model.MessageDTO;

import java.nio.charset.StandardCharsets;


@Service
public class NotificationServiceImpl implements NotificationService {
    Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public NotificationServiceImpl(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(MessageDTO messageDTO) {
        logger.info(">>>>>>>>>> NotificationService NotificationServiceImpl: sendEmail");

        try {
            logger.info("Start ... sending email");
            //context
            Context context = new Context();
            context.setVariable("name", messageDTO.getToName());
            context.setVariable("content", messageDTO.getContent());
            String html = templateEngine.process("welcome-email", context);

            //send email
            MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, StandardCharsets.UTF_8.name());
            helper.setTo(messageDTO.getTo());
            helper.setText(html, true);
            helper.setSubject(messageDTO.getSubject());
            helper.setFrom(messageDTO.getFrom());
            this.javaMailSender.send(mimeMessage);
            logger.info("End ... email send successfully");
        } catch (MessagingException e) {
            logger.error(e.getMessage());
        }

    }
}
