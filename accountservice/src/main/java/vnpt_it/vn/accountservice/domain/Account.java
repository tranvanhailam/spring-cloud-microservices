package vnpt_it.vn.accountservice.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "account")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    private String password;
    @Column(name = "username", unique = true)
    private String username;
    @ElementCollection
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "account_id"))
    @Column(name = "role")
    private Set<String> roles;
}
