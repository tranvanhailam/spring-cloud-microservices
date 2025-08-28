package vnpt_it.vn.notificationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private String from;
    private String to;
    private String toName;
    private String subject;
    private String content;
}
