package vnpt_it.vn.accountservice.domain.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnpt_it.vn.accountservice.util.constant.GenderEnum;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResAccountDTO {
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
    private Role role;
    private Company company;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Role {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Company {
        private long id;
        private String name;
    }
}

