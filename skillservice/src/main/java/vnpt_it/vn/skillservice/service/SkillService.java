package vnpt_it.vn.skillservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vnpt_it.vn.skillservice.domain.Skill;
import vnpt_it.vn.skillservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.skillservice.exception.ExistsException;
import vnpt_it.vn.skillservice.exception.NotFoundException;

public interface SkillService {
    Skill handlCreateSkill(Skill skill) throws ExistsException;

    Skill handleUpdateSkill(Skill skill) throws NotFoundException, ExistsException;

    void handleDeleteSkill(long id) throws NotFoundException;

    Skill handleGetSkillById(long id) throws NotFoundException;

    ResultPaginationDTO handleGetAllSkills(Specification<Skill> specification, Pageable pageable);
}
