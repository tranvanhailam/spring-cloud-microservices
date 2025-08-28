package vnpt_it.vn.subscriberservice.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import vnpt_it.vn.subscriberservice.domain.MessageJobIntroductionDTO;
import vnpt_it.vn.subscriberservice.domain.Subscriber;
import vnpt_it.vn.subscriberservice.domain.SubscriberSkill;
import vnpt_it.vn.subscriberservice.job.JobDTO;
import vnpt_it.vn.subscriberservice.job.JobService;
import vnpt_it.vn.subscriberservice.kafka.KafkaProducer;
import vnpt_it.vn.subscriberservice.repository.SubscriberRepository;
import vnpt_it.vn.subscriberservice.skill.SkillDTO;
import vnpt_it.vn.subscriberservice.skill.SkillService;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {
    private final SubscriberRepository subscriberRepository;
    private final SkillService skillService;
    private final JobService jobService;
    private final SubscriberSkillService subscriberSkillService;
    private final KafkaProducer kafkaProducer;

    public EmailServiceImpl(SubscriberRepository subscriberRepository, SkillService skillService, JobService jobService, SubscriberSkillService subscriberSkillService, KafkaProducer kafkaProducer) {
        this.subscriberRepository = subscriberRepository;
        this.skillService = skillService;
        this.jobService = jobService;
        this.subscriberSkillService = subscriberSkillService;
        this.kafkaProducer = kafkaProducer;
    }


    @Override
    public List<MessageJobIntroductionDTO> handleSendEmail() {
        List<MessageJobIntroductionDTO> messageJobIntroductionDTOs= new  ArrayList<>();

        List<Subscriber> subscribers = subscriberRepository.findAll();
        if(!subscribers.isEmpty() ){
            for(Subscriber subscriber : subscribers){
                List<SubscriberSkill> subscriberSkills=  this.subscriberSkillService.handleGetBySubscriberId(subscriber.getId());
                List<SkillDTO> skillDTOs= new ArrayList<>();
                subscriberSkills.forEach(subscriberSkill -> {
                    SkillDTO skillDTO = this.skillService.getSkillById(subscriberSkill.getSkillId()).getData();
                    skillDTOs.add(skillDTO);
                });

                if(!skillDTOs.isEmpty()){
                    List<JobDTO> jobDTOsFinal= new ArrayList<>();
                    skillDTOs.forEach(skillDTO -> {
                            List<JobDTO> jobDTOs = this.jobService.getJobsBySkillId(skillDTO.getId()).getData();
                            jobDTOsFinal.addAll(jobDTOs);
                    });
                    if(!jobDTOsFinal.isEmpty()){
                        MessageJobIntroductionDTO messageJobIntroductionDTO = new MessageJobIntroductionDTO();
                        messageJobIntroductionDTO.setToEmail(subscriber.getEmail());
                        messageJobIntroductionDTO.setToName(subscriber.getName());
                        messageJobIntroductionDTO.setSubject("New Job Matches Just for You!");
                        List<MessageJobIntroductionDTO.Job> jobs = new ArrayList<>();
                        jobDTOsFinal.forEach(jobDTO -> {
                            MessageJobIntroductionDTO.Job job= new MessageJobIntroductionDTO.Job();
                            job.setId(jobDTO.getId());
                            job.setName(jobDTO.getName());
                            job.setLevel(jobDTO.getLevel());
                            job.setLocation(jobDTO.getLocation());
                            job.setSalary(jobDTO.getSalary());

                            MessageJobIntroductionDTO.Job.Company company= new MessageJobIntroductionDTO.Job.Company();
                            company.setId(jobDTO.getCompany().getId());
                            company.setName(jobDTO.getCompany().getName());
                            job.setCompany(company);

                            jobs.add(job);
                        });
                        messageJobIntroductionDTO.setJobs(jobs);
                        messageJobIntroductionDTOs.add(messageJobIntroductionDTO);
                        this.kafkaProducer.sendMessageJobIntroduction(messageJobIntroductionDTO);
                    }
                }
            }
        }
        return messageJobIntroductionDTOs;
    }
}
