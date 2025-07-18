package vnpt_it.vn.accountservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnpt_it.vn.accountservice.domain.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
