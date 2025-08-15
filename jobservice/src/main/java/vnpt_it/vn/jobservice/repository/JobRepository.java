package vnpt_it.vn.jobservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnpt_it.vn.jobservice.domain.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findAll(Specification<Job> specification, Pageable pageable);
}
