package vnpt_it.vn.statisticservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "statistic")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Statistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "message")
    private String message;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_date")
    private Instant createdDate;

    @PrePersist
    public void handleBeforeCreate() {
        this.createdDate = Instant.now();
    }

}
