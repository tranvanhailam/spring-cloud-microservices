package vnpt_it.vn.subscriberservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import vnpt_it.vn.subscriberservice.domain.MessageJobIntroductionDTO;
import vnpt_it.vn.subscriberservice.job.JobDTO;
import vnpt_it.vn.subscriberservice.job.JobService;
import vnpt_it.vn.subscriberservice.service.EmailService;

import java.util.List;

@RestController
public class TestController {
    private final JobService jobService;
    private final EmailService emailService;

    public TestController(JobService jobService, EmailService emailService) {
        this.jobService = jobService;
        this.emailService = emailService;
    }

    @GetMapping("/jobs/skill/{id}")
    public ResponseEntity<List<JobDTO>> getJobsBySkillId(@PathVariable("id") long id) {
        List<JobDTO> jobDTOs= this.jobService.getJobsBySkillId(id).getData();
        return ResponseEntity.status(HttpStatus.OK).body(jobDTOs);
    }

    @GetMapping("/test")
    @Transactional
    public ResponseEntity<List<MessageJobIntroductionDTO>> getTest() {
        List<MessageJobIntroductionDTO> messageJobIntroductionDTOs= this.emailService.handleSendEmail();
        return ResponseEntity.status(HttpStatus.OK).body(messageJobIntroductionDTOs);
    }
}
