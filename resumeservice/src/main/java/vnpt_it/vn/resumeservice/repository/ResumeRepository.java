package vnpt_it.vn.resumeservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnpt_it.vn.resumeservice.domain.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume,Long> {
    Page<Resume> findAll(Specification<Resume> specification, Pageable pageable);
}
