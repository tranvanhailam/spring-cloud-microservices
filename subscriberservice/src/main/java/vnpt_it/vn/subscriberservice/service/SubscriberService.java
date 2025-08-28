package vnpt_it.vn.subscriberservice.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vnpt_it.vn.subscriberservice.domain.Subscriber;
import vnpt_it.vn.subscriberservice.domain.res.ResSubscriberDTO;
import vnpt_it.vn.subscriberservice.domain.res.ResultPaginationDTO;
import vnpt_it.vn.subscriberservice.exception.NotFoundException;

public interface SubscriberService {
    ResSubscriberDTO handleCreateSubscriber(Subscriber subscriber);
    ResSubscriberDTO handleUpdateSubscriber(Subscriber subscriber) throws NotFoundException;
    void handleDeleteSubscriber(long id) throws NotFoundException;
    ResSubscriberDTO handleGetSubscriberById(long id) throws NotFoundException;
    ResultPaginationDTO handleGetAllSubscribers(Specification<Subscriber> specification, Pageable pageable);
}
