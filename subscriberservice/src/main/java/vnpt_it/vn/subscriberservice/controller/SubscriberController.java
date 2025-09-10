package vnpt_it.vn.subscriberservice.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vnpt_it.vn.subscriberservice.domain.Subscriber;
import vnpt_it.vn.subscriberservice.domain.res.ResSubscriberDTO;
import vnpt_it.vn.subscriberservice.domain.res.RestResponse;
import vnpt_it.vn.subscriberservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.subscriberservice.exception.NotFoundException;
import vnpt_it.vn.subscriberservice.service.SubscriberService;
import vnpt_it.vn.subscriberservice.util.annotation.ApiMessage;
import vnpt_it.vn.subscriberservice.util.annotation.ValidationCreateSubscriber;
import vnpt_it.vn.subscriberservice.util.annotation.ValidationUpdateSubscriber;

@RestController
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Create subscriber")
    @ValidationCreateSubscriber
    public ResponseEntity<ResSubscriberDTO> createSubscriber(@Valid @RequestBody Subscriber subscriber) {
        ResSubscriberDTO resSubscriberDTO = this.subscriberService.handleCreateSubscriber(subscriber);
        return ResponseEntity.status(HttpStatus.CREATED).body(resSubscriberDTO);
    }

    @PutMapping("/subscribers")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Update subscriber")
    @ValidationUpdateSubscriber
    public ResponseEntity<ResSubscriberDTO> updateSubscriber(@Valid @RequestBody Subscriber subscriber) throws NotFoundException {
        ResSubscriberDTO resSubscriberDTO = this.subscriberService.handleUpdateSubscriber(subscriber);
        return ResponseEntity.status(HttpStatus.OK).body(resSubscriberDTO);
    }

    @DeleteMapping("/subscribers/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','SCOPE_internal')")
    @ApiMessage("Delete subscriber")
    public ResponseEntity<?> deleteSubscriber(@PathVariable long id) throws NotFoundException {
        this.subscriberService.handleDeleteSubscriber(id);
        RestResponse<String> restResponse = new RestResponse<>();
        restResponse.setMessage("Delete subscriber successfully");
        restResponse.setStatusCode(HttpStatus.OK.value());
        return ResponseEntity.status(HttpStatus.OK).body(restResponse);
    }

    @GetMapping("/subscribers/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','SCOPE_internal')")
    @ApiMessage("Get subscriber by id")
    public ResponseEntity<ResSubscriberDTO> getSubscriberById(@PathVariable long id) throws NotFoundException {
        ResSubscriberDTO resSubscriberDTO = this.subscriberService.handleGetSubscriberById(id);
        return ResponseEntity.status(HttpStatus.OK).body(resSubscriberDTO);
    }

    @GetMapping("/subscribers")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER','SCOPE_internal')")
    @ApiMessage("Get all subscribers")
    public ResponseEntity<ResultPaginationDTO> getAllSubscribers(Pageable pageable, @Filter Specification<Subscriber> specification) {
        ResultPaginationDTO resultPaginationDTO = this.subscriberService.handleGetAllSubscribers(specification, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(resultPaginationDTO);
    }
}
