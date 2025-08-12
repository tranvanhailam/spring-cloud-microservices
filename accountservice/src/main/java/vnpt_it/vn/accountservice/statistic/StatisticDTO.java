package vnpt_it.vn.accountservice.statistic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticDTO {
    private long id;
    private String message;
    private Instant createdDate;

    public StatisticDTO(String message) {
        this.message = message;
    }
}
