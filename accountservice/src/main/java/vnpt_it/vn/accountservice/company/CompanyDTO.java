package vnpt_it.vn.accountservice.company;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyDTO {
    private long id;
    private String name;
//    private String address;
//    private String description;
//    private String logo;
//    private String createdAt;
//    private String updatedAt;
//    private String createdBy;
//    private String updatedBy;
}
