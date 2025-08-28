package vnpt_it.vn.subscriberservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnpt_it.vn.subscriberservice.skill.SkillDTO;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "subscribers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Subscriber {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;
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
    @Transient
    private List<SkillDTO> skills;

//    @ManyToMany(fetch = FetchType.LAZY)
//    @JsonIgnoreProperties(value = {"subscribers"})
//    @JoinTable(name = "subscriber_skill", joinColumns = @JoinColumn(name = "subscriber_id"), inverseJoinColumns = @JoinColumn(name = "skill_id"))
//    private List<Skill> skills;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate
    public void handleBeforeUpdate() {
        this.updatedAt = Instant.now();
    }
}
