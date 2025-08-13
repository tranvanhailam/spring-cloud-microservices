package vnpt_it.vn.jobservice.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserInfo {
    private String sub;
    private String name;
    private String email;
    private String preferred_username;
}
