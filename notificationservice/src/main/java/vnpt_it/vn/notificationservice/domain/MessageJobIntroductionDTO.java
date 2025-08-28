package vnpt_it.vn.notificationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageJobIntroductionDTO {
    private String toEmail;
    private String toName;
    private String subject;
//    private String message;
    private List<Job> jobs;
//    private boolean status;
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Job {
        private long id;
        private String name;
        private String level;
        private String location;
        private double salary;
        private Company company;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
         public static class Company {
            private long id;
            private String name;
        }
    }
}
