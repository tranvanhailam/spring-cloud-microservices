package vnpt_it.vn.resumeservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnpt_it.vn.resumeservice.util.constant.StatusEnum;

import java.time.Instant;

@Entity
@Table(name = "resumes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "email")
    private String email;
    @Column(name = "url")
    private String url;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusEnum status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    @Column(name = "created_at")
    private Instant createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    @Column(name = "updated_at")
    private Instant updatedAt;
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "updated_by")
    private String updatedBy;
    @JoinColumn(name = "account_id")
    private long accountId;
    @JoinColumn(name = "job_id")
    private long jobId;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
    }
}
