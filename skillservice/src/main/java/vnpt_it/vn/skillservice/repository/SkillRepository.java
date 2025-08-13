package vnpt_it.vn.skillservice.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnpt_it.vn.skillservice.domain.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill,Long> {
    boolean existsByName(String name);

    Page<Skill> findAll(Specification<Skill> specification, Pageable pageable);
}
