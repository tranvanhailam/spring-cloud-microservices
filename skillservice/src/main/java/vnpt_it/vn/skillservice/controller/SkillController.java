package vnpt_it.vn.skillservice.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.hibernate.internal.build.AllowNonPortable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vnpt_it.vn.skillservice.domain.Skill;
import vnpt_it.vn.skillservice.domain.res.RestResponse;
import vnpt_it.vn.skillservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.skillservice.exception.ExistsException;
import vnpt_it.vn.skillservice.exception.NotFoundException;
import vnpt_it.vn.skillservice.service.SkillService;
import vnpt_it.vn.skillservice.util.annotation.ApiMessage;

@RestController
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_internal')")
    @ApiMessage("Create skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws ExistsException {
        Skill skillCreated = this.skillService.handlCreateSkill(skill);
        return ResponseEntity.status(HttpStatus.CREATED).body(skillCreated);
    }

    @PutMapping("/skills")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_internal')")
    @ApiMessage("Update skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) throws ExistsException, NotFoundException {
        Skill skillUpdated = this.skillService.handleUpdateSkill(skill);
        return ResponseEntity.status(HttpStatus.OK).body(skillUpdated);
    }

    @DeleteMapping("/skills/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER','SCOPE_internal')")
    @ApiMessage("Delete skill")
    public ResponseEntity<?> deleteSkill(@PathVariable("id") long id) throws NotFoundException {
        this.skillService.handleDeleteSkill(id);
        RestResponse<String> restResponse = new RestResponse<>();
        restResponse.setStatusCode(HttpStatus.OK.value());
        restResponse.setMessage("Delete account successfully!");
        return ResponseEntity.status(HttpStatus.OK).body(restResponse);
    }

    @GetMapping("/skills/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Get skill by id")
    public ResponseEntity<Skill> getSkillById(@PathVariable("id") long id) throws NotFoundException {
        Skill skill = this.skillService.handleGetSkillById(id);
        return ResponseEntity.status(HttpStatus.OK).body(skill);
    }

    @GetMapping("/skills")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Get all skills")
    public ResponseEntity<ResultPaginationDTO> getAllSkills(Pageable pageable, @Filter Specification<Skill> specification) {
        ResultPaginationDTO resultPaginationDTO = this.skillService.handleGetAllSkills(specification, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }

}
