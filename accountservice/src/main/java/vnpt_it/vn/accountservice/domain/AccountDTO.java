package vnpt_it.vn.accountservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import vnpt_it.vn.accountservice.util.constant.GenderEnum;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private long id;
    private String name;
    private String email;
//    private String password;
    private String address;
    private int age;
    private GenderEnum gender;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private long companyId;
    private long roleId;
}
