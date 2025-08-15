package vnpt_it.vn.resumeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnpt_it.vn.resumeservice.domain.Resume;

@Repository
public interface ResumeRepository extends JpaRepository<Resume,Long> {
}
