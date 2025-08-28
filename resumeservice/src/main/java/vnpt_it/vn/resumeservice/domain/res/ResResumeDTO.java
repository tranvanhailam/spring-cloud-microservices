package vnpt_it.vn.resumeservice.domain.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnpt_it.vn.resumeservice.util.constant.StatusEnum;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResResumeDTO {
    private long id;
    private String email;
    private String url;
    private StatusEnum status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private Account account;
    private Job job;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Account{
        private long id;
        private String email;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Job{
        private long id;
        private String name;
    }

}
