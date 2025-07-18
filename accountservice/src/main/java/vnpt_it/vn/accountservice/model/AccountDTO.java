package vnpt_it.vn.accountservice.model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private long id;
    private String name;
    private String password;
    private String username;
    private Set<String> roles;
}
