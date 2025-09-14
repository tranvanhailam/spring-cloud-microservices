package vnpt_it.vn.skillservice.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vnpt_it.vn.skillservice.auth.AuthService;
import vnpt_it.vn.skillservice.domain.Skill;
import vnpt_it.vn.skillservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.skillservice.exception.ExistsException;
import vnpt_it.vn.skillservice.exception.NotFoundException;
import vnpt_it.vn.skillservice.repository.SkillRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;
    private final AuthService authService;

    public SkillServiceImpl(SkillRepository skillRepository, AuthService authService) {
        this.skillRepository = skillRepository;
        this.authService = authService;
    }

    @CachePut(value = "skills", key = "#skill.id")
    @Override
    public Skill handlCreateSkill(Skill skill) throws ExistsException {
        if (this.skillRepository.existsByName(skill.getName())) {
            throw new ExistsException("Skill with name " + skill.getName() + " already exists");
        }
        skill.setCreatedBy(this.authService.getUserInfo().getSub());
        return this.skillRepository.save(skill);
    }

    @CachePut(value = "skills", key = "#skill.id")
    @Override
    public Skill handleUpdateSkill(Skill skill) throws NotFoundException, ExistsException {
        Optional<Skill> optionalSkill = this.skillRepository.findById(skill.getId());
        if (optionalSkill.isPresent()) {
            Skill skillToUpdate = optionalSkill.get();
//        skillToUpdate.setId(skill.getId());
            if (this.skillRepository.existsByName(skill.getName())) {
                throw new ExistsException("Skill with name " + skill.getName() + " already exists");
            }
            skillToUpdate.setName(skill.getName());
            skillToUpdate.setUpdatedBy(this.authService.getUserInfo().getSub());
            return this.skillRepository.save(skillToUpdate);
        }
        throw new NotFoundException("Skill with id " + skill.getId() + " not found");
    }

    @CacheEvict(value = "skills", key = "#id")
    @Override
    public void handleDeleteSkill(long id) throws NotFoundException {
        Optional<Skill> optionalSkill = this.skillRepository.findById(id);
        if (!optionalSkill.isPresent()) {
            throw new NotFoundException("Skill with id " + id + " not found");
        }
        this.skillRepository.deleteById(id);
    }

    @Cacheable(value = "skills", key = "#id")
    @Override
    public Skill handleGetSkillById(long id) throws NotFoundException {
        Optional<Skill> optionalSkill = this.skillRepository.findById(id);
        if (optionalSkill.isPresent()) {
            return optionalSkill.get();
        }
        throw new NotFoundException("Skill with id " + id + " not found");
    }

    @Override
    public ResultPaginationDTO handleGetAllSkills(Specification<Skill> specification, Pageable pageable) {
        Page<Skill> skillPage = this.skillRepository.findAll(specification, pageable);

        List<Skill> skills = skillPage.getContent().stream()
                .map(skill -> {
                    Skill skillShow = new Skill();
                    skillShow.setId(skill.getId());
                    skillShow.setName(skill.getName());
                    skillShow.setCreatedAt(skill.getCreatedAt());
                    skillShow.setUpdatedAt(skill.getUpdatedAt());
                    skillShow.setCreatedBy(skill.getCreatedBy());
                    skillShow.setUpdatedBy(skill.getUpdatedBy());
                    return skillShow;
                }).collect(Collectors.toList());

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPageNumber(skillPage.getNumber() + 1);
        meta.setPageSize(skillPage.getSize());
        meta.setTotalPages(skillPage.getTotalPages());
        meta.setTotalElements(skillPage.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(skills);
        return resultPaginationDTO;
    }
}
