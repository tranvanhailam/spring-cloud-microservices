package vnpt_it.vn.subscriberservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnpt_it.vn.subscriberservice.domain.Subscriber;

@Repository
public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {
    Page<Subscriber> findAll(Specification<Subscriber> specification, Pageable pageable);
}
